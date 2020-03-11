package com.crud.service;

import com.crud.model.Post;
import com.crud.model.User;
import com.crud.repository.PostRepository;
import com.crud.repository.UserRepository;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import org.apache.commons.collections4.IterableUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PostService {

    private PostRepository postRepository;
    private UserRepository userRepository;

    @JacksonXmlElementWrapper(localName = "posts")
    private List<Post> temp = new ArrayList<>();

    @Autowired
    public PostService(PostRepository postRepository,UserRepository userRepository){
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    public Boolean createPost(String userId,Post post){

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
        //postRepository.findAll().forEach(temp::add);
        Pageable pageable = PageRequest.of(pageNo,pageSize, Sort.by("date").descending());
        temp = postRepository.findAll(pageable).toList();
        return temp;
    }

    public List<Post> retrieveAllPostByUserId(String userId){
        return postRepository.findAllPostByUserId(userId);
    }

    public Boolean updatePost(Post post, UserDetails userDetails){
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


}

