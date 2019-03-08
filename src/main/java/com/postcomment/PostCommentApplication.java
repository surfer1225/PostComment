package com.postcomment;

import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.postcomment.contract.internal.request.VoteRequest;
import com.postcomment.entity.Comment;
import com.postcomment.entity.Post;
import com.postcomment.mockdb.DataCenter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@SpringBootApplication(scanBasePackages = "com.postcomment")
public class PostCommentApplication implements CommandLineRunner {
    public static void main(String[] args) {
        SpringApplication.run(PostCommentApplication.class, args);
    }

    @Autowired
    DataCenter dataCenter;

    private Gson gson = new Gson();

    @Override
    public void run(String... args) throws Exception {
        ClassLoader classLoader = getClass().getClassLoader();
        loadPosts(classLoader);
        loadComments(classLoader);
        loadVotes(classLoader);
    }

    private void loadVotes(ClassLoader classLoader) throws URISyntaxException {
        URI uri = classLoader.getResource("votes.json").toURI();
        Path votePath = Paths.get(uri);
        try(BufferedReader reader = Files.newBufferedReader(votePath)) {
            Type type= new TypeToken<List<Tmp>>() {}.getType();
            List<Tmp> voteList= gson.fromJson(reader, type);
            voteList.forEach(vote->{
                if(!Strings.isNullOrEmpty(vote.uuid)){
                    dataCenter.changeVote(UUID.fromString(vote.uuid),vote.upvote?1:-1);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadComments(ClassLoader classLoader) throws URISyntaxException, IOException {
        URI uri = classLoader.getResource("comments.json").toURI();
        Path commentPath = Paths.get(uri);
        String collect = Files.readAllLines(commentPath).stream().collect(Collectors.joining("\n"));
        Type type = new TypeToken<List<Comment>>() {}.getType();
        List<Comment> commentList = gson.fromJson(collect, type);
        commentList.forEach(comment -> dataCenter.addComment(comment));
    }

    private void loadPosts(ClassLoader classLoader) throws URISyntaxException, IOException {
        URI uri = classLoader.getResource("posts.json").toURI();
        Path postPath = Paths.get(uri);
        String collect = Files.readAllLines(postPath).stream().collect(Collectors.joining("\n"));
        Type type = new TypeToken<List<Post>>() {}.getType();
        List<Post> postList = gson.fromJson(collect, type);
        postList.forEach(post -> dataCenter.addPost(post));
    }


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    private class Tmp{
        private boolean upvote;
        private String uuid;
    }
}
