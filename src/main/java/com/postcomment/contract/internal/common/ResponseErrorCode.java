package com.postcomment.contract.internal.common;

public enum ResponseErrorCode {

    E000("UNKNOWN"),

    // request error
    ER00("EMPTY_REQUEST"),
    ER01("INVALID_REQUEST"),
    ER02("ID Does not exits"),
    ;

    private String msg;

    ResponseErrorCode(String message) {
        this.msg = message;
    }
}
