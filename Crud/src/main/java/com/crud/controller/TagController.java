package com.crud.controller;

import com.crud.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
public class TagController {

    private TagService tagService;

    @Autowired
    public TagController(TagService tagService){
        this.tagService = tagService;
    }

    @GetMapping(value = "/posts/tags",produces = {"application/json","application/xml"},consumes = {"application/json","application/xml"})
    public ResponseEntity retrieveAllTag(){
        return new ResponseEntity<>(tagService.getAllTag(), HttpStatus.OK);
    }

    @GetMapping(value = "/posts/tag/{name}",produces = {"application/json","application/xml"},consumes = {"application/json","application/xml"})
    public ResponseEntity retrieveAllPostByTagName(@PathVariable String name,@RequestParam("pageNo") Integer pageNo, @RequestParam("pageSize") Integer pageSize){
        return new ResponseEntity<>(tagService.getAllPostByTagName(pageNo,pageSize,name), HttpStatus.OK);
    }

    @GetMapping(value = "/posts/tag/trending",produces = {"application/json","application/xml"},consumes = {"application/json","application/xml"})
    public ResponseEntity retrieveTrendingTags(@RequestParam("num") Integer count){
        return new ResponseEntity<>(tagService.getTrendingTags(count), HttpStatus.OK);
    }

    @DeleteMapping(value = "/posts/tag/{tagId}",produces = {"application/json","application/xml"},consumes = {"application/json","application/xml"})
    public ResponseEntity deleteTagFromAllPostOfUser(@PathVariable("tagId")Long tagId,@RequestParam("userId")String userId){
        return tagService.deleteTagFromAllPostOfUser(tagId,userId)? new ResponseEntity<>("Deleted",HttpStatus.OK)
                :new ResponseEntity<>("No content",HttpStatus.NOT_FOUND);
    }

    @DeleteMapping(value = "/post/tag",produces = {"application/json","application/xml"},consumes = {"application/json","application/xml"})
    public ResponseEntity deleteTagFromPost(@RequestParam("postId")Long postId,@RequestParam("tagId")Long tagId){
        return tagService.deleteTagFromPost(postId,tagId)? new ResponseEntity<>("Deleted",HttpStatus.OK)
                :new ResponseEntity<>("No content",HttpStatus.NOT_FOUND);
    }
}
