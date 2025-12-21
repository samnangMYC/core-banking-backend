package com.trendy.cbs.http;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
    private int status;
    private T data;
    private String message;

    public static <T> ApiResponse<T> created(T data) {
        return new ApiResponse<>(201, data, "Created successfully");
    }
}
