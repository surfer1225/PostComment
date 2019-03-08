package com.postcomment.handler;


import com.postcomment.contract.internal.request.TopRequest;
import com.postcomment.contract.internal.response.PostsResponse;
import com.postcomment.mockdb.DataCenter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GetTopPostHandler extends BaseHandler<TopRequest, PostsResponse, Object> {
    @Autowired
    DataCenter dataCenter;

    //when number is -1, get all posts
    @Override
    protected PostsResponse processRequest(TopRequest request, PostsResponse response, Object operationData) {
        if(request.getNumber()==-1){
            response.setPosts(dataCenter.getPost());
        }else{
            response.setPosts(dataCenter.getTopPost(request.getNumber()));
        }
        return response;
    }
}
