package com.trendy.cbs.exception;

public class DuplicationResource extends RuntimeException {
    public DuplicationResource(String message){
        super(message);
    }
}
