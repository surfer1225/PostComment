package com.postcomment.controller;

import com.postcomment.contract.internal.request.TopRequest;
import com.postcomment.contract.internal.request.VoteRequest;
import com.postcomment.contract.internal.response.BaseResponse;
import com.postcomment.handler.GetTopPostHandler;
import com.postcomment.handler.VoteHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Slf4j
@RestController
public class PostcommentController {

    @Autowired
    GetTopPostHandler getTopPostHandler;

    @Autowired
    VoteHandler voteHandler;


    @RequestMapping(path="/", method = RequestMethod.GET)
    public BaseResponse all(){
        return getTopPostHandler.execute(TopRequest.builder().number(-1).build());
    }

    @RequestMapping(path="/top", method = RequestMethod.GET)
    public BaseResponse top(){
        return getTopPostHandler.execute(TopRequest.builder().number(10).build());
    }

    @RequestMapping(path="/upvote/{uuid}", method = RequestMethod.POST)
    public BaseResponse upvote(@PathVariable("uuid") UUID id){
        return voteHandler.execute(VoteRequest.builder().upvote(true).uuid(id).build());
    }

    @RequestMapping(path="/downvote/{uuid}", method = RequestMethod.POST)
    public BaseResponse downvote(@PathVariable("uuid") UUID id){
        return voteHandler.execute(VoteRequest.builder().upvote(false).uuid(id).build());
    }


}
