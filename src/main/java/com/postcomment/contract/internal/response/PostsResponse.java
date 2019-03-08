package com.postcomment.contract.internal.response;

import com.postcomment.entity.Post;
import lombok.Data;

import java.util.List;

@Data
public class PostsResponse extends BaseResponse {
    List<Post> posts;
}
