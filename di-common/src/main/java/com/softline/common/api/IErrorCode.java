package com.softline.common.api;

/**
 * 封装API的错误码
 * Created by dong on 2020/11/23.
 */
public interface IErrorCode {
    long getCode();

    String getMessage();
}
