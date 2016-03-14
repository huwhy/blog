package com.aichebaba.weixin.sdk.exception;

public class BusinessException extends RuntimeException {
    public BusinessException(){}
    public BusinessException(String msg, Throwable e){
        super(msg, e);
    }
}
