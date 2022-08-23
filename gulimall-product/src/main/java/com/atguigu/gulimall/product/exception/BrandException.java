package com.atguigu.gulimall.product.exception;

import com.atguigu.common.exception.BizCodeException;
import com.atguigu.common.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;

/**
 * @author
 * @create 2022-08-13-14:42
 */
@Slf4j
@RestControllerAdvice(basePackages = "com.atguigu.gulimall.product.controller")
public class BrandException {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public R brandException(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
        HashMap<String, String> map = new HashMap<>();
        bindingResult.getFieldErrors().forEach((item) -> {
            map.put(item.getField(), item.getDefaultMessage());
        });
        return R.error(BizCodeException.VALID_EXCEPTION.getCode(), BizCodeException.VALID_EXCEPTION.getMessage())
                .put("data", map);
    }

    @ExceptionHandler(Throwable.class)
    public R generalException(Throwable e) {
        log.error("错误！====", e);
        return R.error(BizCodeException.UNKOWN_EXCEPTION.getCode(), BizCodeException.UNKOWN_EXCEPTION.getMessage());
    }
}
