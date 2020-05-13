package com.crud.service;

import com.crud.model.Post;
import com.crud.model.Tag;
import com.crud.model.User;
import com.crud.model.dao.PostDAO;
import com.crud.repository.PostRepository;
import com.crud.repository.TagRepository;
import com.crud.repository.UserRepository;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.IterableUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
@CacheConfig(cacheNames = {"allPost"})
public class PostService {

    private PostRepository postRepository;
    private UserRepository userRepository;
    private TagRepository tagRepository;

    @JacksonXmlElementWrapper(localName = "posts")
    private List<Post> temp;

    @Autowired
    public PostService(PostRepository postRepository,UserRepository userRepository,TagRepository tagRepository){
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.tagRepository = tagRepository;
    }


    @CacheEvict(allEntries = true)
    public Boolean createPost(String userId, PostDAO postDAO){

        Post post = new Post();
        post.setIsPublished(postDAO.getIsPublished());
        post.setBody(postDAO.getBody());
        post.setTitle(postDAO.getTitle());
        post.setIsDrafted(postDAO.getIsDrafted());
        post.setPublishDate(postDAO.getPublishDate());


        Set<Tag> finalTagSet = new HashSet<>();

        Iterator<Tag> it = postDAO.getAlternateTags().iterator();
        while (it.hasNext()){
            Tag tag = it.next();
            Optional<Tag> exist = Optional.ofNullable(tagRepository.findByName(tag.getName()));

            if(exist.isPresent())
             finalTagSet.add(exist.get());
            
            else
            finalTagSet.add(tagRepository.save(tag));
        }

        finalTagSet.addAll(postDAO.getTags());
        post.setTags(finalTagSet);

        Optional<User> tempUser = userRepository.findById(userId);

        return tempUser.map(t -> {
            post.setUser(t);
            postRepository.save(post);
            log.info("Post Created");
            return true;
        }).orElseGet(() -> false);
    }

    public Optional<Post> retrievePostById(Long postId){
        return postRepository.findById(postId);
    }

    @Cacheable(key = "{#pageNo,#pageSize}")
    public List<Post> retrieveAllPost(int pageNo,int pageSize){
        temp = new ArrayList<>();

        Pageable pageable = PageRequest.of(pageNo,pageSize, Sort.by("date").descending());
        temp = postRepository.findAllByPage(pageable);

        log.info("All Post Retrieved");
        return temp;
    }

    public List<Post> searchPost(String pattern,int pageNo, int pageSize){
        Pageable pageable = PageRequest.of(pageNo,pageSize, Sort.by("date").descending());
        return postRepository.searchPost(pattern,pageable);
    }

    public List<Post> retrieveAllPostByUserId(String userId){
        return postRepository.findAllPostByUserId(userId);
    }


    @CachePut(key = "#post.id",cacheResolver = "customCacheResolver")
    public Boolean updatePost(Post post, UserDetails userDetails){
        System.out.println(post.getId());

        Optional<Post> tempPost = postRepository.findById(post.getId());

        return tempPost.map(temp -> {

            if(userDetails.getUsername().equals(temp.getUser().getUserId())) {
                if (post.getTitle().replaceAll("\\s+", "").isEmpty()) {
                    post.setTitle(temp.getTitle());
                }
                if (post.getBody().replaceAll("\\s+", "").isEmpty()) {
                    post.setBody(temp.getBody());
                }

                post.setDate(temp.getDate());
                post.setUser(temp.getUser());
                post.setTags(temp.getTags());
                post.setPublishDate(temp.getPublishDate());

                postRepository.save(post);
                log.info("Post Updated");
                return true;
            }
            else{
                log.error("Post Update Failed");
                return false;
            }
        }).orElseGet(() -> false);

    }

    @CachePut(key = "#postId",cacheResolver = "customCacheResolver")
    public Boolean deletePost(Long postId,UserDetails userDetails){

        Optional<Post> tempPost = postRepository.findById(postId);

        try {
            if(tempPost.isPresent()){
               if(tempPost.get().getUser().getUserId().equals(userDetails.getUsername())){
                   postRepository.deleteById(postId);
                   log.info("Post Deleted");
                   return true;
               }
               else {
                   log.error("Post Deletion Failed");
                   return false;
               }
            }
            else {
                log.error("Post Deletion Failed");
                return false;
            }


        }catch (Exception e){
            log.error("Post Deletion Failed");
            return false;
        }

    }


    public int totalDataSize(){
        Iterable<Post> posts = postRepository.findAll();
        log.info("Post Size Checked");
        return IterableUtils.size(posts);
    }


    public void publishPost(){
        log.info("Post Publisher Invoked");
        ArrayList<Post> allPost = (ArrayList<Post>) postRepository.findAll();
        for(int i=0;i<allPost.size();i++){
            Post temp = allPost.get(i);
            if(!temp.getIsDrafted()){
                if(!temp.getIsPublished() && temp.getPublishDate().before(new Date())){
                    temp.setIsPublished(true);
                    postRepository.save(temp);
                }
            }
        }
    }


}

