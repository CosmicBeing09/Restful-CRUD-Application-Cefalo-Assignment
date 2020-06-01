package com.crud.service;

import com.crud.model.Post;
import com.crud.model.User;
import com.crud.model.Comment;
import com.crud.repository.CommentRepository;
import com.crud.repository.PostRepository;
import com.crud.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CommentService {

    private CommentRepository commentRepository;
    private UserRepository userRepository;
    private PostRepository postRepository;

    @Autowired
    public CommentService(CommentRepository commentRepository,UserRepository userRepository,PostRepository postRepository){
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    public Boolean addComment(String userId,Long postId,String text){
        Optional<User> tempUser = userRepository.findById(userId);
        if (tempUser.isPresent()){
            Optional<Post> tempPost = postRepository.findById(postId);
            return tempPost.map(data -> {
                try {
                    List<Comment> comments = data.getComments();
                    Comment comment = new Comment();
                    comment.setText(text);
                    comment.setPost(data);
                    comment.setUser(tempUser.get());

                    comments.add(commentRepository.save(comment));

                    data.setComments(comments);
                    Post updatedPost = postRepository.save(data);
                }catch (Exception e){return false;}
                return true;
            }).orElseGet(() -> false);
        }
        return false;
    }

    public Boolean deleteComment(Long postId,Long commentId){

        Optional<Comment> comment = commentRepository.findById(commentId);
        Optional<Post> post =  postRepository.findById(postId);

        return comment.map(tempComment -> {
            try {
                Post tempPost = post.get();
                tempPost.getComments().remove(tempComment);
                postRepository.save(tempPost);
                commentRepository.delete(tempComment);
            }catch (Exception e){return  false;}
            return true;
        }).orElseGet(() -> false);
    }

}
