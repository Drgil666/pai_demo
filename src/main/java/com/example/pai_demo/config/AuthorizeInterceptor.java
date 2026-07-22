package com.example.pai_demo.config;

import com.alibaba.fastjson.JSON;
import com.example.pai_demo.annoations.Authorize;
import com.example.pai_demo.exception.ErrorException;
import com.example.pai_demo.model.User;
import com.example.pai_demo.model.vo.ResponseVO;
import com.example.pai_demo.service.TokenService;
import com.example.pai_demo.service.UserService;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

import static com.example.pai_demo.constants.errorDict.*;

/**
 * @author GilbertYoung
 * @date 2026/07/22 10:00
 */
@Component
public class AuthorizeInterceptor implements HandlerInterceptor {
    @Resource
    private TokenService tokenService;
    @Resource
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;

        // 类级别注解
        Authorize classAnno = handlerMethod.getBeanType().getAnnotation(Authorize.class);
        // 方法级别注解（方法级别覆盖类级别）
        Authorize methodAnno = handlerMethod.getMethodAnnotation(Authorize.class);
        Authorize authorize = methodAnno != null ? methodAnno : classAnno;

        if (authorize == null) {
            return true;
        }

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            writeUnauthorized(response, GET_TOKEN_ERROR);
            return false;
        }

        String token = authHeader.substring(7);
        Integer userId;
        try {
            userId = tokenService.getUserIdByToken(token);
        } catch (ErrorException e) {
            writeUnauthorized(response, e.getMessage());
            return false;
        }

        User user = userService.getUserById(userId);
        if (user == null) {
            writeUnauthorized(response, USER_NOT_EXIST_ERROR);
            return false;
        }

        int maxPrivilege = Arrays.stream(authorize.value()).max().orElse(Authorize.USER);
        if (user.getPrivilege() > maxPrivilege) {
            writeUnauthorized(response, PRIVILEGE_ERROR);
            return false;
        }

        return true;
    }

    private void writeUnauthorized(HttpServletResponse response, String msg) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write(JSON.toJSONString(new ResponseVO<>(-1, msg, null)));
    }
}
