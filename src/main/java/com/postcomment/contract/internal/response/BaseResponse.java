package com.postcomment.contract.internal.response;

import com.postcomment.contract.internal.common.ResponseError;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseResponse {
    private UUID referenceId; //to match the referenceId in request
    private boolean success;
    private List<ResponseError> errorList = new ArrayList<>();
}
