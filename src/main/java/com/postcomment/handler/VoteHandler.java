package com.postcomment.handler;


import com.postcomment.contract.internal.common.ValidationError;
import com.postcomment.contract.internal.request.VoteRequest;
import com.postcomment.contract.internal.response.BaseResponse;
import com.postcomment.mockdb.DataCenter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.postcomment.contract.internal.common.ResponseErrorCode.ER02;

@Component
public class VoteHandler extends BaseHandler<VoteRequest, BaseResponse, Object> {

    @Autowired
    DataCenter dataCenter;

    @Override
    protected BaseResponse processRequest(VoteRequest request, BaseResponse response, Object operationData) {
        int diff = request.isUpvote()?1:-1;

        int i = dataCenter.changeVote(request.getUuid(), diff);
        if(i!=1){
            response.getErrorList().add(new ValidationError(ER02));
            return response;
        }
        return response;
    }
}
