package com.postcomment.handler;

import com.postcomment.contract.internal.common.ResponseError;
import com.postcomment.contract.internal.common.ValidationError;
import com.postcomment.contract.internal.request.BaseRequest;
import com.postcomment.contract.internal.response.BaseResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.postcomment.contract.internal.common.ResponseErrorCode.E000;
import static com.postcomment.contract.internal.common.ResponseErrorCode.ER01;

public abstract class BaseHandler<TReq extends BaseRequest, TRes extends BaseResponse, TOpe> {
    private Logger log = LoggerFactory.getLogger(this.getClass());
    public TRes execute(TReq request) {
        TRes response = null;
        TOpe operationData;
        try {
            if(request==null){
                response.getErrorList().add(new ValidationError(ER01.toString()));
                log.info("Empty request received, return immediately.");
                return response;
            }
            if(request.getReferenceId()==null){
                request.setReferenceId(UUID.randomUUID());
            }

            log.info("[{}] Handler {}", request.getReferenceId().toString(), this.getClass().getSimpleName());
            log.info("[{}] Start generating objects.",request.getReferenceId().toString());
            response = getResponseObj();
            response.setErrorList(new ArrayList<>());

            //set reference uuid for response to track
            response.setReferenceId(request.getReferenceId());

            operationData = getOperationDataObj();
            log.info("[{}] End generating objects.", request.getReferenceId());

            log.info("[{}] Start validating request", request.getReferenceId());
            response = validateRequest(request, response);
            if (anyError(response))
                return response;
            log.info("[{}] End validating request", request.getReferenceId());

            log.info("[{}] Start validating Data",request.getReferenceId());
            response = validateData(request, response, operationData);
            if (anyError(response))
                return response;
            log.info("[{}] End validating Data", request.getReferenceId());

            log.info("[{}] Start Processing request.", request.getReferenceId());
            response = processRequest(request, response, operationData);
            if (anyError(response))
                return response;
            log.info("[{}] End Processing request.", request.getReferenceId());

        } catch (Exception e) {
            log.info("Exception received, please check exception log.");
            log.error(String.format("[%s] Exception caught while executing the service: [%s]", request.getReferenceId(),
                    this.getClass().getSimpleName()), e);
            if (response != null) {
                response.getErrorList().add(new ValidationError(E000));
            }
        }

        response.setSuccess(!anyError(response));
        return response;
    }

    protected abstract TRes processRequest(TReq request, TRes response, TOpe operationData);

    protected TRes validateData(TReq request, TRes response, TOpe operationData) {
        return response;
    }

    protected TRes validateRequest(TReq request, TRes response) {
        return response;
    }

    private TRes getResponseObj() throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        Type[] actualTypeArguments = ((ParameterizedType) this.getClass().getGenericSuperclass())
                .getActualTypeArguments();
        if (actualTypeArguments == null || actualTypeArguments.length != 3)
            throw new IllegalStateException("Something is seriously wrong in getting response obj");
        String typeName = actualTypeArguments[1].getTypeName();
        Class<?> aClass = Class.forName(typeName);
        return (TRes) aClass.newInstance();
    }

    private TOpe getOperationDataObj() throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        Type[] actualTypeArguments = ((ParameterizedType) this.getClass().getGenericSuperclass())
                .getActualTypeArguments();
        if (actualTypeArguments == null || actualTypeArguments.length != 3)
            throw new IllegalStateException("Something is seriously wrong in getting operational data");
        String typeName = actualTypeArguments[2].getTypeName();
        Class<?> aClass = Class.forName(typeName);
        return (TOpe) aClass.newInstance();
    }

    protected boolean anyError(TRes tres) {
        List<ResponseError> errorList = tres.getErrorList();
        return errorList != null && errorList.size() != 0;
    }
}
