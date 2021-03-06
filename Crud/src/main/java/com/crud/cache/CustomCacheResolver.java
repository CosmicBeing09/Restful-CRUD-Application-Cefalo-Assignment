package com.crud.cache;

import com.crud.model.Post;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.interceptor.CacheOperationInvocationContext;
import org.springframework.cache.interceptor.CacheResolver;
import org.springframework.cache.interceptor.SimpleKey;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import java.util.*;


@Component
@Slf4j
public class CustomCacheResolver implements CacheResolver {

    private static final String CACHE_NAME = "allPost";

    @Autowired
    private CacheManager cacheManager;

    @SuppressWarnings("unchecked")
    @Override
    public Collection<? extends Cache> resolveCaches(CacheOperationInvocationContext<?> cacheOperationInvocationContext) {

        log.info(Arrays.toString(cacheOperationInvocationContext.getArgs()));

        String method = cacheOperationInvocationContext.getMethod().toString();
        Post post = null;
        Long postId = null;
        boolean isPostContained = false;

        if(method.contains("update")) {
            //get the updated post
            Object[] args = cacheOperationInvocationContext.getArgs();
            post = (Post) args[0];
        }
        else if(method.contains("delete")){
            //get the post Id to delete
            Object[] args = cacheOperationInvocationContext.getArgs();
            postId = (Long) args[0];
        }



        //read the cache
        Cache cache = cacheManager.getCache(CACHE_NAME);

        //get the concurrent cache map in key-value pair
        assert cache != null;
        Map<SimpleKey, List<Post>> map = (Map<SimpleKey, List<Post>>) cache.getNativeCache();

        //Convert to set to iterate
        Set<Map.Entry<SimpleKey, List<Post>>> entrySet = map.entrySet();
        Iterator<Map.Entry<SimpleKey, List<Post>>> itr = entrySet.iterator();

        //if a iterated entry is a list then it is our desired data list!!! Yayyy
        Map.Entry<SimpleKey, List<Post>> entry = null;
        while (itr.hasNext()){
            entry = itr.next();
            if(entry instanceof List) break;
        }

        //get the list
        if(entry!=null) {
            try {
            List<Post> postList = entry.getValue();

            if (method.contains("update")) {
                //update it
                for (Post temp : postList) {
                    assert post != null;
                    if (temp.getId().equals(post.getId())) {
                        postList.remove(temp);
                        isPostContained = true;
                        break;
                    }
                }
                assert post != null;
                log.info(post.getIsDrafted().toString());
                log.info(String.valueOf(post.getPublishDate().before(new Date())));
                if (isPostContained && !post.getIsDrafted() && post.getPublishDate().before(new Date()))
                    postList.add(post);
            } else if (method.contains("delete")) {
                //delete it
                for (Post temp : postList) {
                    if (temp.getId().equals(postId)) {
                        postList.remove(temp);
                        break;
                    }
                }
            }


            //update the cache!! :D
            cache.put(entry.getKey(), postList);
        }catch (Exception e){
                return new ArrayList<>(Collections.singletonList(cacheManager.getCache(CACHE_NAME)));
            }
        }

        return new ArrayList<>(Collections.singletonList(cacheManager.getCache(CACHE_NAME)));
    }
}

