package com.postcomment.contract.internal.request;

import lombok.Data;

import java.util.UUID;

@Data
public class BaseRequest {
    private UUID referenceId;
}
