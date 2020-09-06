package com.sample.springredditclone.mapper;


import com.sample.springredditclone.dto.SubredditDto;
import com.sample.springredditclone.model.Post;
import com.sample.springredditclone.model.Subreddit;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SubredditMapper {

    @Mapping(target =  "postCount", expression = "java(mapPosts(subreddit.getPosts()))")
    SubredditDto mapSubredditToDto(Subreddit subreddit);

    default Integer mapPosts(List<Post> postList){
        return postList.size();
    }

    @InheritInverseConfiguration
    @Mapping(target = "posts", ignore = false)
    Subreddit mapDtoToSubreddit(SubredditDto subredditDto);
}
