package com.crud.service;

import com.crud.model.Post;
import com.crud.model.Tag;
import com.crud.model.User;
import com.crud.model.dao.PostDAO;
import com.crud.repository.PostRepository;
import com.crud.repository.TagRepository;
import com.crud.repository.UserRepository;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import org.apache.commons.collections4.IterableUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
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
            Tag tag = tagRepository.save(it.next());
            finalTagSet.add(tag);
        }

        finalTagSet.addAll(postDAO.getTags());
        post.setTags(finalTagSet);

        Optional<User> tempUser = userRepository.findById(userId);

        return tempUser.map(t -> {
            post.setUser(t);
            postRepository.save(post);
            return true;
        }).orElseGet(() -> false);
    }

    public Optional<Post> retrievePostById(Long postId){
        return postRepository.findById(postId);
    }

    public List<Post> retrieveAllPost(int pageNo,int pageSize){
        temp = new ArrayList<>();

        Pageable pageable = PageRequest.of(pageNo,pageSize, Sort.by("date").descending());
        temp = postRepository.findAllByPage(pageable);

        return temp;
    }

    public List<Post> searchPost(String pattern,int pageNo, int pageSize){
        Pageable pageable = PageRequest.of(pageNo,pageSize, Sort.by("date").descending());
        return postRepository.searchPost(pattern,pageable);
    }

    public List<Post> retrieveAllPostByUserId(String userId){
        return postRepository.findAllPostByUserId(userId);
    }

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
                postRepository.save(post);
                return true;
            }
            else{
                return false;
            }
        }).orElseGet(() -> false);

    }

    public Boolean deletePost(Long postId,UserDetails userDetails){

        Optional<Post> tempPost = postRepository.findById(postId);

        try {
            if(tempPost.isPresent()){
               if(tempPost.get().getUser().getUserId().equals(userDetails.getUsername())){
                   postRepository.deleteById(postId);
                   return true;
               }
               else {
                   return false;
               }
            }
            else {
                return false;
            }


        }catch (Exception e){
            return false;
        }

    }

    public int totalDataSize(){
        Iterable<Post> posts = postRepository.findAll();
        return IterableUtils.size(posts);
    }

    public void publishPost(){
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

