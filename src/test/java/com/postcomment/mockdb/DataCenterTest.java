package com.postcomment.mockdb;

import com.postcomment.entity.Post;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.Assert.assertEquals;

public class DataCenterTest {
    @InjectMocks
    private DataCenter dc;

    @Before
    public void initMocks(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getPostsTest() {
        dc.postTable.clear();
        Post p1 = Post.builder()
                .author("author")
                .comments(new ArrayList<>())
                .uuid(UUID.randomUUID())
                .content("content")
                .votes(10)
                .build();
        Post p2 = Post.builder()
                .author("author1")
                .comments(new ArrayList<>())
                .uuid(UUID.randomUUID())
                .content("content1")
                .votes(11)
                .build();
        dc.postTable.put(UUID.randomUUID(), p1);
        dc.postTable.put(UUID.randomUUID(), p2);

        List<Post> l = new LinkedList<>();
        l.add(p2);
        l.add(p1);
        l.sort((o1, o2)-> o1.getVotes()>o2.getVotes()?1:o1.getVotes()==o2.getVotes()?0:-1);
        List<Post> l2 = dc.getPost();
        l2.sort((o1, o2)-> o1.getVotes()>o2.getVotes()?1:o1.getVotes()==o2.getVotes()?0:-1);
        assertEquals(l,l2);
    }

    @Test
    public void getTopPostsTest() {
        dc.postTable.clear();
        Post p1 = Post.builder()
                .author("author")
                .comments(new ArrayList<>())
                .uuid(UUID.randomUUID())
                .content("content")
                .votes(10)
                .build();
        Post p2 = Post.builder()
                .author("author1")
                .comments(new ArrayList<>())
                .uuid(UUID.randomUUID())
                .content("content1")
                .votes(11)
                .build();
        dc.postTable.put(UUID.randomUUID(), p1);
        dc.postTable.put(UUID.randomUUID(), p2);
        dc.postIndexVote.add(p1);
        dc.postIndexVote.add(p2);

        List<Post> l = new LinkedList<>();
        l.add(p2);
        assertEquals(l,dc.getTopPost(1));
    }

    @Test
    public void changeVoteTest() {
        dc.postTable.clear();
        UUID u1 = UUID.randomUUID();
        Post p1 = Post.builder()
                .author("author")
                .comments(new ArrayList<>())
                .uuid(u1)
                .content("content")
                .votes(10)
                .build();
        dc.postTable.put(u1, p1);
        dc.postIndexVote.add(p1);

        assertEquals(1, dc.changeVote(u1,1));
    }
}
