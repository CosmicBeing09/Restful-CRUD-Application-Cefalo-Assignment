package com.crud.controller;

import com.crud.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
public class CommentController {

    private CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService){
        this.commentService = commentService;
    }

    @PostMapping(value = "/post/comment",consumes = {"text/plain"},produces = {"application/json","application/xml"})
    @ResponseBody
    public ResponseEntity addComment(@RequestParam("userId") String userId,@RequestParam("postId") Long postId,@RequestBody String text){
        return commentService.addComment(userId, postId, text)? new ResponseEntity<>("Comment added", HttpStatus.CREATED)
                : new ResponseEntity<>("Bad Request",HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping(value = "/post/comment",consumes = {"application/json","application/xml"},produces = {"application/json","application/xml"})
    @ResponseBody
    public ResponseEntity deleteComment(@RequestParam("postId") Long postId,@RequestParam("commentId") Long commentId){
        return commentService.deleteComment(postId, commentId) ? new ResponseEntity<>("Comment Deleted",HttpStatus.OK)
                : new ResponseEntity<>("Bad Request",HttpStatus.BAD_REQUEST);
    }
}
