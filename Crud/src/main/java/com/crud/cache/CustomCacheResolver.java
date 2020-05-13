package com.crud.cache;

import com.crud.model.Post;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.interceptor.CacheOperationInvocationContext;
import org.springframework.cache.interceptor.CacheResolver;
import org.springframework.cache.interceptor.SimpleKey;
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
        Cache ehCacheCache = cacheManager.getCache(CACHE_NAME);

        //get the concurrent cache map in key-value pair
        assert ehCacheCache != null;
        Map<SimpleKey, List<Post>> map = (Map<SimpleKey, List<Post>>) ehCacheCache.getNativeCache();

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
        assert entry != null;
        List<Post> postList = entry.getValue();

        if(method.contains("update")) {
            //update it
            for (Post temp : postList) {
                assert post != null;
                if (temp.getId().equals(post.getId())) {
                    postList.remove(temp);
                    break;
                }
            }
            postList.add(post);
        }
        else if(method.contains("delete")){
            //delete it
            for (Post temp : postList) {
                if (temp.getId().equals(postId)) {
                    postList.remove(temp);
                    break;
                }
            }
        }


        //update the cache!! :D
        ehCacheCache.put(entry.getKey(),postList);

        return new ArrayList<>(Collections.singletonList(cacheManager.getCache(CACHE_NAME)));
    }
}

