package com.crud.service;

import com.crud.model.Post;
import com.crud.model.User;
import com.crud.repository.PostRepository;
import com.crud.repository.UserRepository;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import org.springframework.beans.factory.annotation.Autowired;
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

    public List<Post> retrieveAllPost(){
        temp = new ArrayList<>();
        postRepository.findAll().forEach(temp::add);
        return temp;
    }

    public List<Post> retrieveAllPostByUserId(String userId){
        return postRepository.findAllPostByUserId(userId);
    }

    public Boolean updatePost(Post post){
        Optional<Post> tempPost = postRepository.findById(post.getId());

        return tempPost.map(temp -> {
            if (post.getTitle().replaceAll("\\s+","").isEmpty()){
                post.setTitle(temp.getTitle());
            }
            if(post.getBody().replaceAll("\\s+","").isEmpty()){
                post.setBody(temp.getBody());
            }
            post.setDate(temp.getDate());
            post.setUser(temp.getUser());
            postRepository.save(post);
            return true;
        }).orElseGet(() -> false);

    }

    public Boolean deletePost(Long postId){

        try {
            postRepository.deleteById(postId);
            return true;
        }catch (Exception e){
            return false;
        }

    }


}

