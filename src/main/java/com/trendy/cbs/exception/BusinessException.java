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
    public static BusinessException badRequest(ErrorCode code, String message) {
        return new BusinessException(message, code, 400);
    }

    public static BusinessException unauthorized(ErrorCode code, String message) {
        return new BusinessException(message, code, 401);
    }

    public static BusinessException forbidden(ErrorCode code, String message) {
        return new BusinessException(message, code, 403);
    }

    public static BusinessException notFound(ErrorCode code, String message) {
        return new BusinessException(message, code, 404);
    }

    public static BusinessException internal(ErrorCode code, String message) {
        return new BusinessException(message, code, 500);
    }
}

