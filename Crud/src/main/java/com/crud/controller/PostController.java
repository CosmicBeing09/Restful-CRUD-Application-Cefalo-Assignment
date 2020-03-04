package com.crud.controller;

import com.crud.model.Post;
import com.crud.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    public PostController(PostService postService){
        this.postService = postService;
    }


    @PostMapping(value = "/create-post/{userId}",consumes = {"application/json","application/xml"})
    @ResponseBody
    public String createPost(@RequestBody Post post, @PathVariable String userId){
        return postService.createPost(userId,post);
    }


    @GetMapping(value = "/retrieve-all-post",produces = {"application/json","application/xml"})
    public List<Post> retrieveAllPost(){
        return postService.retrieveAllPost();
    }


    @GetMapping(value = "/retrieve-post-by-id/{post-id}",produces = {"application/json","application/xml"})
    public ResponseEntity retrievePostById(@PathVariable("post-id") Long postId){

        Optional<Post> post = postService.retrievePostById(postId);

        return post.map(value -> new ResponseEntity<Post>(value, HttpStatus.OK)).orElseGet(() ->  ResponseEntity.notFound().build());

    }

    @PutMapping(value = "/update-post",consumes = {"application/json","application/xml"})
    public ResponseEntity updatePost(@RequestBody Post post){
        return postService.updatePost(post);
    }

    @GetMapping(value = "/retrieve-post-of-user/{userId}")
    public List<Post> retrieveAllPostByUserId(@PathVariable String userId){
        return postService.retrieveAllPostByUserId(userId);
    }

    @DeleteMapping(value = "/delete-post/{post-id}")
    public ResponseEntity deletePost(@PathVariable("post-id")Long postId){
        return postService.deletePost(postId);
    }
}

