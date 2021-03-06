package com.crud.controller;

import com.crud.model.Post;
import com.crud.model.dao.PostDAO;
import com.crud.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
public class PostController{

    private PostService postService;

    @Autowired
    public PostController(PostService postService){
        this.postService = postService;
    }


    @PostMapping(value = "/post/{userId}",consumes = {"application/json","application/xml"},produces = {"application/json","application/xml"})
    @ResponseBody
    public ResponseEntity<?> createPost(@Valid @RequestBody PostDAO post, @PathVariable String userId){
        return postService.createPost(userId,post)? new ResponseEntity<>("Post created",HttpStatus.CREATED)
                : new ResponseEntity<>("Bad Request",HttpStatus.BAD_REQUEST);
    }

    @GetMapping(value = "/posts",produces = {"application/json","application/xml"},consumes = {"application/json","application/xml"})
    public List<Post> retrieveAllPost(@RequestParam("pageNo") int pageNo, @RequestParam("pageSize") int pageSize){
        return postService.retrieveAllPost(pageNo,pageSize);
    }

    @GetMapping(value = "/posts/search",produces = {"application/json","application/xml"},consumes = {"application/json","application/xml"})
    public List<Post> searchPost(@RequestParam("pageNo") int pageNo, @RequestParam("pageSize") int pageSize, @RequestParam("text")String pattern){
        return postService.searchPost(pattern,pageNo,pageSize);
    }

    @GetMapping(value = "/posts/editor-post/{userId}",produces = {"application/json","application/xml"},consumes = {"application/json","application/xml"})
    public List<Post> getAllEditorPostByUserId(@RequestParam("pageNo") int pageNo, @RequestParam("pageSize") int pageSize, @PathVariable String userId){
        return postService.getAllEditorPostByUserId(pageNo,pageSize,userId);
    }

    @GetMapping(value = "/post/{postId}",produces = {"application/json","application/xml"},consumes = {"application/json","application/xml"})
    public ResponseEntity<?> retrievePostById(@PathVariable("postId") Long postId){
        Optional<Post> post = postService.retrievePostById(postId);
        return post.map(value -> ResponseEntity.ok().eTag("\""+value.getVersion()+"\"").body(value)).orElseGet(() ->  ResponseEntity.notFound().build());
    }

    @PutMapping(value = "/post/view/{postId}",produces = {"application/json","application/xml"},consumes = {"application/json","application/xml"})
    public ResponseEntity<?> countView(@PathVariable("postId") Long postId){
        Integer count = postService.incrementView(postId);
        return new ResponseEntity<>(count,HttpStatus.OK);
    }

    @PutMapping(value = "/post",consumes = {"application/json","application/xml"},produces = {"application/json","application/xml"})
    public ResponseEntity<?> updatePost(WebRequest request, @Valid @RequestBody Post post, Authentication authentication){
        String ifMatchValue = request.getHeader("If-Match");
        if (ifMatchValue == null) {
            return ResponseEntity.badRequest().build();
        }
        if (!ifMatchValue.equals("\"" + post.getVersion() + "\"")) {
            return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).build();
        }

        try {
            return  postService.updatePost(post, (UserDetails) authentication.getPrincipal())? new ResponseEntity<>("Updated Successfully",HttpStatus.OK)
                    :new ResponseEntity<>("No content",HttpStatus.BAD_REQUEST);
        }catch (OptimisticLockingFailureException e){ return  new ResponseEntity<>("Post already updated! Get or Reload the page again and try again!",
                HttpStatus.CONFLICT);}

    }

    @GetMapping(value = "/user-posts/{userId}",produces = {"application/json","application/xml"},consumes = {"application/json","application/xml"})
    public ResponseEntity<?> retrieveAllPostByUserId(@PathVariable String userId){
        return new ResponseEntity<>(postService.retrieveAllPostByUserId(userId),HttpStatus.OK);
    }

    @DeleteMapping(value = "/post/{postId}",produces = {"application/json","application/xml"},consumes = {"application/json","application/xml"})
    public ResponseEntity<?> deletePost(@PathVariable("postId")Long postId,Authentication authentication){

        return postService.deletePost(postId,(UserDetails)authentication.getPrincipal())? new ResponseEntity<>("Deleted",HttpStatus.OK)
                :new ResponseEntity<>("No content",HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/posts/mostCommented",produces = {"application/json","application/xml"},consumes = {"application/json","application/xml"})
    public List<Post> retrieveMostCommentedPost(@RequestParam("count") int count){
        return postService.getMostCommentedPost(count);
    }

    @GetMapping(value = "/posts/size",produces = {"application/json","application/xml"},consumes = {"application/json","application/xml"})
    public ResponseEntity<?> totalDataSize(){
        return new ResponseEntity<>(postService.totalDataSize(),HttpStatus.OK);
    }

    @GetMapping(value = "/posts/flush-cache",produces = {"application/json","application/xml"})
    public ResponseEntity<?> flushCache(){
        return postService.flushCache()? new ResponseEntity<>("Hard Reloaded",HttpStatus.OK)
                :new ResponseEntity<>("Failed to hard reload",HttpStatus.BAD_REQUEST);
    }

    @Scheduled(fixedDelay = 30000)
    public void publishPost(){
        postService.publishPost();
    }
}

