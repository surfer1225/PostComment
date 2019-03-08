package com.postcomment.contract.internal.common;

public class ValidationError extends ResponseError {
    public ValidationError(String errorCode) {
        super(errorCode);
    }

    public ValidationError(ResponseErrorCode validationErrorCode) {
        super(validationErrorCode.toString());
    }
}
