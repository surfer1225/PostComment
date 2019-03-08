package com.postcomment.mockdb;

import com.postcomment.entity.Comment;
import com.postcomment.entity.Post;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * TO model the behavior of real DB
 * Deep copy method is implemented to make sure the objects returned are not the same objects in the data structure
 * This also obeys defensive programming mindset
 */
@Component
public class DataCenter {
    Map<UUID, Post> postTable = new HashMap<>();
    TreeSet<Post> postIndexVote = new TreeSet<>((o1, o2)-> o1.getVotes()>o2.getVotes()?1:o1.getVotes()==o2.getVotes()?0:-1);
    TreeMap<UUID, TreeSet<Comment>> commentForeignKey = new TreeMap<>();
    Map<UUID, Comment> commentTable = new HashMap<>();

    // add one more method to get all posts
    public synchronized List<Post> getPost() {
        List<Post> postList = new ArrayList<>();
        for(Post post: postTable.values()){
            Post post1 = deepCopyPost(post);
            postList.add(post1);
        }
        return postList;
    }

    public synchronized void addPost(Post post){
        Post post1 = deepCopyPost(post);
        postTable.put(post1.getUuid(),post1);
        postIndexVote.add(post1);
    }

    public synchronized void addComment(Comment comment){
        Comment comment1 = deepCopyComment(comment);
        if(postTable.containsKey(comment1.getPostUuid())){
            if(!commentForeignKey.containsKey(comment1.getPostUuid())){
                commentForeignKey.put(comment1.getPostUuid(),new TreeSet<>((o1, o2)-> o1.getVotes()>o2.getVotes()?1:o1.getVotes()==o2.getVotes()?0:-1));
            }
            commentForeignKey.get(comment1.getPostUuid()).add(comment1);
            commentTable.put(comment1.getUuid(),comment1);
        }
    }

    private int changeVotePost(UUID uuid, int diff) {
        Post post = postTable.get(uuid);
        postIndexVote.remove(post);
        post.setVotes(post.getVotes()+diff);
        postIndexVote.add(post);
        return 1;
    }

    private int changeVoteComment(UUID uuid, int diff) {
        Comment comment = commentTable.get(uuid);
        if(comment==null){
            return 0;
        }
        TreeSet<Comment> comments = commentForeignKey.get(comment.getPostUuid());
        if(comments!=null){
            comments.remove(comment);
            comment.setVotes(comment.getVotes()+diff);
            comments.add(comment);
            return 1;
        }
        return 0;
    }

    public synchronized  List<Post> getTopPost(int number) {
        List<Post> postList = new ArrayList<>();

        NavigableSet<Post> posts1 = postIndexVote.descendingSet();
        int i = 0;
        for (Post post : posts1) {
            Post post1 = deepCopyPostWithoutComment(post);
            postList.add(post1);
            TreeSet<Comment> comments1 = commentForeignKey.get(post.getUuid());
            if(comments1!=null){
                NavigableSet<Comment> comments = comments1.descendingSet();
                int commentCnt = 0;
                for (Comment comment : comments) {
                    post1.getComments().add(deepCopyComment(comment));
                    commentCnt++;
                    if (commentCnt >= 3) {
                        break;
                    }
                }
            }
            i++;
            if (i >= number) {
                break;
            }
        }
        return postList;
    }

    private Post deepCopyPost(Post post){
        Post build = Post.builder()
                .author(post.getAuthor())
                .comments(new ArrayList<>())
                .uuid(post.getUuid())
                .content(post.getContent())
                .votes(post.getVotes())
                .build();
        for(Comment comment: post.getComments()){
            build.getComments().add(deepCopyComment(comment));
        }
        return build;
    }

    private Post deepCopyPostWithoutComment(Post post) {
        return Post.builder()
                .author(post.getAuthor())
                .comments(new ArrayList<>())
                .uuid(post.getUuid())
                .content(post.getContent())
                .votes(post.getVotes())
                .build();
    }

    private Comment deepCopyComment(Comment comment) {
        return Comment.builder()
                .author(comment.getAuthor())
                .uuid(comment.getUuid())
                .content(comment.getContent())
                .votes(comment.getVotes())
                .postUuid(comment.getPostUuid())
                .build();
    }

    public synchronized int changeVote(UUID uuid, int diff) {
        if (postTable.containsKey(uuid)) {
            return changeVotePost(uuid, diff);
        } else {
            return changeVoteComment(uuid, diff);
        }
    }

}
