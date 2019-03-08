package com.postcomment.controller;

import com.postcomment.contract.internal.request.TopRequest;
import com.postcomment.contract.internal.response.PostsResponse;
import com.postcomment.entity.Post;
import com.postcomment.handler.GetTopPostHandler;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class PostCommentControllerTest {
    @InjectMocks
    private PostcommentController postcommentController;

    @Mock
    private GetTopPostHandler getTopPostHandler;

    @Test
    public void getTopPostsTest() {
        List<Post> l = new LinkedList<>();
        l.add(Post.builder()
                .author("author")
                .comments(new ArrayList<>())
                .uuid(UUID.randomUUID())
                .content("content")
                .votes(10)
                .build());
        PostsResponse response = new PostsResponse();
        response.setPosts(l);
        when(getTopPostHandler.execute(any(TopRequest.class))).thenReturn(response);
        assertEquals(response, postcommentController.top());
    }
}
