package com.softline.common.exception;

import com.softline.common.api.IErrorCode;

/**
 * 断言处理类，用于抛出各种API异常
 * Created by dong on 2020/11/24.
 */
public class Asserts {
    public static void fail(String message) {
        throw new ApiException(message);
    }

    public static void fail(IErrorCode errorCode) {
        throw new ApiException(errorCode);
    }
}
