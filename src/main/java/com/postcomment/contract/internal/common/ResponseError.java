package com.postcomment.contract.internal.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Data
@AllArgsConstructor
public class ResponseError {
    @Getter
    private String errorCode;
}
