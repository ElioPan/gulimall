package com.atguigu.common.exception;

/**
 * @author
 * @create 2022-08-13-15:13
 */
public enum BizCodeException {
    VALID_EXCEPTION(10001,"数据校验异常"),
    UNKOWN_EXCEPTION(10000,"系统未知异常");
    private int code;
    private String message;
    BizCodeException(int code, String message){
        this.code =code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
