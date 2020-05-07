package com.crud.service;

import com.crud.model.Post;
import com.crud.model.Tag;
import com.crud.repository.PostRepository;
import com.crud.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TagService {

    private TagRepository tagRepository;
    private PostRepository postRepository;

    @Autowired
    public TagService(TagRepository tagRepository,PostRepository postRepository){
        this.tagRepository = tagRepository;
        this.postRepository = postRepository;
    }

    public List<Tag> getAllTag(){
        return tagRepository.findAll();
    }

    public Boolean deleteTag(Long id){
        Optional<Tag> tempTag = tagRepository.findById(id);

        try {
            if(tempTag.isPresent()){
                    ArrayList<Post> allPost = (ArrayList<Post>) postRepository.findAll();
                    for(Post post : allPost){
                        if(post.getTags().contains(tempTag.get())){
                            post.getTags().remove(tempTag.get());
                            postRepository.save(post);
                        }
                    }
                    tagRepository.deleteById(id);
                    return true;
            }
            else {
                return false;
            }


        }catch (Exception e){
            return false;
        }
    }

    public List<Post> getAllPostByTagName(Integer pageNo,Integer pageSize,String name){
        Pageable pageable = PageRequest.of(pageNo,pageSize, Sort.by("date").descending());
        return postRepository.findAllByTags_NameAndIsDraftedFalseAndIsPublishedTrue(pageable,name).toList();
    }

    public List<Tag> getTrendingTags(Integer noOfTags){
        List<Tag> allTag = tagRepository.findAll();
        allTag.sort(Comparator.comparingLong(tag -> tag.getPosts().size()));
        Collections.reverse(allTag);
        return allTag.subList(0,Math.min(noOfTags,allTag.size()));
    }
}
