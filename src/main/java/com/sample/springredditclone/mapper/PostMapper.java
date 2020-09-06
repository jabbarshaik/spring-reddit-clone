package com.sample.springredditclone.mapper;

import com.sample.springredditclone.dto.PostRequest;
import com.sample.springredditclone.dto.PostResponse;
import com.sample.springredditclone.model.Post;
import com.sample.springredditclone.model.Subreddit;
import com.sample.springredditclone.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PostMapper {




    @Mapping(target = "createdDate", expression = "java(java.time.Instant.now())")
    @Mapping(target = "description", source = "postRequest.description")
    @Mapping(target = "subreddit", source = "subreddit")
    @Mapping(target = "user", source = "user")
    Post mapDtoToPost(PostRequest postRequest, User user, Subreddit subreddit);


    @Mapping(target = "id", source = "postId")
    @Mapping(target = "postName", source = "postName")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "url", source = "url")
    @Mapping(target = "subredditName", source = "subreddit.name")
    @Mapping(target = "userName", source = "user.username")
    PostResponse  postToPostResponse(Post post);



}
