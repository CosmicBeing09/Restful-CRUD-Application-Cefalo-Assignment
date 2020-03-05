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


    @PostMapping(value = "/post/{userId}",consumes = {"application/json","application/xml"},produces = {"application/json","application/xml"})
    @ResponseBody
    public ResponseEntity createPost(@RequestBody Post post, @PathVariable String userId){
        return postService.createPost(userId,post)? new ResponseEntity<>("Post created",HttpStatus.CREATED)
                : new ResponseEntity<>("Bad Request",HttpStatus.BAD_REQUEST);
    }


    @GetMapping(value = "/posts",produces = {"application/json","application/xml"},consumes = {"application/json","application/xml"})
    public List<Post> retrieveAllPost(){
        return postService.retrieveAllPost();
    }


    @GetMapping(value = "/post/{postId}",produces = {"application/json","application/xml"},consumes = {"application/json","application/xml"})
    public ResponseEntity retrievePostById(@PathVariable("postId") Long postId){
        Optional<Post> post = postService.retrievePostById(postId);
        return post.map(value -> new ResponseEntity<Post>(value, HttpStatus.OK)).orElseGet(() ->  ResponseEntity.notFound().build());

    }

    @PutMapping(value = "/post",consumes = {"application/json","application/xml"},produces = {"application/json","application/xml"})
    public ResponseEntity updatePost(@RequestBody Post post){
        return postService.updatePost(post)? new ResponseEntity<>("Updated Successfully",HttpStatus.OK)
                :new ResponseEntity<>("No content",HttpStatus.BAD_REQUEST);
    }

    @GetMapping(value = "/user-posts/{userId}",produces = {"application/json","application/xml"},consumes = {"application/json","application/xml"})
    public ResponseEntity retrieveAllPostByUserId(@PathVariable String userId){
        return new ResponseEntity<>(postService.retrieveAllPostByUserId(userId),HttpStatus.OK);
    }

    @DeleteMapping(value = "/post/{postId}",produces = {"application/json","application/xml"},consumes = {"application/json","application/xml"})
    public ResponseEntity deletePost(@PathVariable("postId")Long postId){
        return postService.deletePost(postId)? new ResponseEntity<>("Deleted",HttpStatus.OK)
                :new ResponseEntity<>("No content",HttpStatus.NOT_FOUND);
    }
}

