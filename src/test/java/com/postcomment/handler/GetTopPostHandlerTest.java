package com.postcomment.handler;

import com.postcomment.contract.internal.request.TopRequest;
import com.postcomment.contract.internal.response.PostsResponse;
import com.postcomment.entity.Post;
import com.postcomment.mockdb.DataCenter;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

public class GetTopPostHandlerTest {
    @InjectMocks
    private GetTopPostHandler handler;

    @Mock
    private DataCenter dc;

    @Before
    public void initMocks(){
        MockitoAnnotations.initMocks(this);
    }

    //tc1: get all posts fro dc
    //tc2: get top 2 posts
    @Test
    public void getPostsTest() {
        TopRequest tr = new TopRequest();
        List<Post> l = new LinkedList<>();
        l.add(Post.builder()
                .author("author")
                .comments(new ArrayList<>())
                .uuid(UUID.randomUUID())
                .content("content")
                .votes(10)
                .build());
        tr.setNumber(-1);
        PostsResponse response1 = new PostsResponse();
        response1.setPosts(l);
        when(dc.getPost()).thenReturn(l);
        assertEquals(response1, handler.processRequest(tr, new PostsResponse(), null));
        tr.setNumber(1);
        l.add(Post.builder()
                .author("author2")
                .comments(new ArrayList<>())
                .uuid(UUID.randomUUID())
                .content("content2")
                .votes(11)
                .build());
        when(dc.getTopPost(2)).thenReturn(l);
        tr.setNumber(2);
        PostsResponse response2 = new PostsResponse();
        response2.setPosts(l);
        assertEquals(response2, handler.processRequest(tr, new PostsResponse(), null));
    }
}
