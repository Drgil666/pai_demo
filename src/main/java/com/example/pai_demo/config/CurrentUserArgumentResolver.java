package com.example.pai_demo.config;

import com.alibaba.fastjson.JSON;
import com.example.pai_demo.annoations.CurrentUser;
import com.example.pai_demo.exception.ErrorException;
import com.example.pai_demo.model.vo.ResponseVO;
import com.example.pai_demo.service.TokenService;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author GilbertYoung
 * @date 2026/07/23 10:00
 */
@Component
public class CurrentUserArgumentResolver implements HandlerMethodArgumentResolver {
    @Resource
    private TokenService tokenService;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(CurrentUser.class)
                && Integer.class.equals(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        String authHeader = webRequest.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            try {
                return tokenService.getUserIdByToken(token);
            } catch (ErrorException e) {
                writeError(webRequest);
                return null;
            }
        }
        writeError(webRequest);
        return null;
    }

    private void writeError(NativeWebRequest webRequest) {
        HttpServletResponse response = webRequest.getNativeResponse(HttpServletResponse.class);
        if (response != null) {
            response.setContentType("application/json;charset=UTF-8");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            try {
                response.getWriter().write(JSON.toJSONString(ResponseVO.createErr(com.example.pai_demo.constants.errorDict.GET_TOKEN_ERROR)));
            } catch (IOException ignored) {
            }
        }
    }
}
