package com.trendy.cbs.exception;

import com.trendy.cbs.enums.ErrorCode;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(callSuper = true)
public class BusinessException extends RuntimeException {

    private final String errorMessage;
    private final ErrorCode errorCode;
    private final int statusCode;

    public BusinessException(String errorMessage, ErrorCode errorCode, int statusCode) {
        super(errorMessage);
        this.errorMessage = errorMessage;
        this.errorCode = errorCode;
        this.statusCode = statusCode;
    }
}

