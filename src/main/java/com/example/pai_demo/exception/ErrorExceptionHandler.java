package com.example.pai_demo.exception;


import com.example.pai_demo.model.vo.ResponseVO;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author DrGilbert
 * @date 2021/4/1 14:40
 */
@ControllerAdvice
public class ErrorExceptionHandler {
    @ExceptionHandler(ErrorException.class)
    @ResponseBody
    public ResponseVO<String> resultError(ErrorException e) {
        return ResponseVO.createErr(e.getCode().getCode(), e.getMessage());
    }
}
