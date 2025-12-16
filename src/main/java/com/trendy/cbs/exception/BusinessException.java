package com.trendy.cbs.exception;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(callSuper = true)
public class BusinessException extends RuntimeException {

    private final String errorCode;
    private final String errorMessage;
    private final int statusCode;

    public BusinessException(String errorMessage, String errorCode, int statusCode) {
        super(errorMessage);
        this.errorMessage = errorMessage;
        this.errorCode = errorCode;
        this.statusCode = statusCode;
    }

}
