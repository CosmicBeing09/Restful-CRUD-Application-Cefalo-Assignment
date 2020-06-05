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
import org.springframework.dao.OptimisticLockingFailureException;
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

        Set<User> authors = new HashSet<>();

            if(postDAO.getAuthorsId()!= null) {
                for (String id : postDAO.getAuthorsId()) {
                    Optional<User> user = userRepository.findById(id);
                    user.ifPresent(authors::add);
                }
            }
            post.setAuthors(authors);


        if(postDAO.getPublishDate()==null) postDAO.setPublishDate(new Date());

        if(postDAO.getPublishDate().before(new Date()))
            post.setIsPublished(true);
        else post.setIsPublished(false);

        post.setBody(postDAO.getBody());
        post.setTitle(postDAO.getTitle());
        post.setIsDrafted(postDAO.getIsDrafted());
        post.setPublishDate(postDAO.getPublishDate());
        post.setNoOfViews(0);
        post.setLastEditedAt(new Date());


        Set<Tag> finalTagSet = new HashSet<>();

        Iterator<Tag> it = postDAO.getNewTags().iterator();
        while (it.hasNext()){
            Tag tag = it.next();
            Optional<Tag> exist = Optional.ofNullable(tagRepository.findByName(tag.getName()));

            if(exist.isPresent())
             finalTagSet.add(exist.get());

            else
            finalTagSet.add(tagRepository.save(tag));
        }

        finalTagSet.addAll(postDAO.getExistingTags());
        post.setTags(finalTagSet);

        Optional<User> tempUser = userRepository.findById(userId);

        return tempUser.map(t -> {
            post.setUser(t);
            post.setLastEditedBy(t);
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
        //return postRepository.findAllPostByUserId(userId);
        return postRepository.findAllByUser_UserId(userId);
    }


    @CachePut(key = "#post.id",cacheResolver = "customCacheResolver")
    public Boolean updatePost(Post post, UserDetails userDetails){
        //System.out.println(post.getId());

        User currentUser = userRepository.getOne(userDetails.getUsername());
        Optional<Post> tempPost = postRepository.findById(post.getId());

        return tempPost.map(temp -> {

            if(userDetails.getUsername().equals(temp.getUser().getUserId()) || temp.getAuthors().contains(currentUser)) {


                if (post.getTitle().replaceAll("\\s+", "").isEmpty()) {
                    post.setTitle(temp.getTitle());
                }
                if (post.getBody().replaceAll("\\s+", "").isEmpty()) {
                    post.setBody(temp.getBody());
                }

                post.setDate(temp.getDate());
                post.setUser(temp.getUser());
                post.setLastEditedBy(currentUser);
                post.setLastEditedAt(new Date());
                post.setTags(temp.getTags());
                //post.setPublishDate(temp.getPublishDate());
                post.setComments(temp.getComments());
                post.setNoOfViews(temp.getNoOfViews());
                post.setAuthors(temp.getAuthors());

                if (post.getPublishDate().before(new Date()) && !post.getIsDrafted())
                    post.setIsPublished(true);
                else post.setIsPublished(false);

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

        User currentUser = userRepository.getOne(userDetails.getUsername());
        Optional<Post> tempPost = postRepository.findById(postId);

        try {
            if(tempPost.isPresent()){
               if(tempPost.get().getUser().getUserId().equals(userDetails.getUsername()) || tempPost.get().getAuthors().contains(currentUser)){
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
        Iterable<Post> posts = postRepository.findAllByIsDraftedFalseAndIsPublishedTrue();
        log.info("Post Size Checked");
        return IterableUtils.size(posts);
    }

    public List<Post> getMostCommentedPost(int count){
        List<Post> allPostPublished = postRepository.findAllByIsDraftedFalseAndIsPublishedTrue();
        allPostPublished.sort(Comparator.comparingLong(post -> post.getComments().size()));
        Collections.reverse(allPostPublished);
        return allPostPublished.subList(0,Math.min(count,allPostPublished.size()));
    }

    public List<Post> getAllEditorPostByUserId(Integer pageNo,Integer pageSize,String userId){
        Pageable pageable = PageRequest.of(pageNo,pageSize, Sort.by("date").descending());
        return postRepository.findAllByAuthors_UserIdAndIsDraftedFalseAndIsPublishedTrue(pageable,userId).toList();
    }

    public Integer incrementView(Long postId){
        Optional<Post> optPost = this.retrievePostById(postId);
        if(optPost.isPresent()){
            Post post = optPost.get();
            post.setNoOfViews(post.getNoOfViews()+1);
            postRepository.save(post);
            return post.getNoOfViews();
        }
        else return 0;
    }

    public void publishPost(){
        log.info("Post Publisher Invoked");
        ArrayList<Post> allPost = (ArrayList<Post>) postRepository.findAllByIsDraftedFalseAndIsPublishedFalse();
        for (Post temp : allPost) {
            if (temp.getPublishDate().before(new Date())) {
                temp.setIsPublished(true);
                postRepository.save(temp);
                this.flushCache();
            }

        }
    }

    @CacheEvict(allEntries = true)
    public Boolean flushCache(){
        log.info("Cache flushed");
        return true;
    }

}

