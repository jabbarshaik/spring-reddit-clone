package com.sample.springredditclone.service;

import com.sample.springredditclone.dto.PostRequest;
import com.sample.springredditclone.dto.PostResponse;
import com.sample.springredditclone.exception.SpringRedditException;
import com.sample.springredditclone.mapper.PostMapper;
import com.sample.springredditclone.model.Post;
import com.sample.springredditclone.model.Subreddit;
import com.sample.springredditclone.model.User;
import com.sample.springredditclone.repository.PostRepository;
import com.sample.springredditclone.repository.SubredditRepository;
import com.sample.springredditclone.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class PostService {


    private final PostRepository postRepository;
    private final SubredditRepository subredditRepository;
    private final UserRepository userRepository;
    private final AuthService authService;
    private final PostMapper postMapper;

    @Transactional
    public PostResponse save(PostRequest postRequest) {

        Subreddit subreddit = subredditRepository
                .findByName(postRequest.getSubredditName())
                .orElseThrow(() -> new SpringRedditException("subreddit not found with name " + postRequest.getSubredditName()));

        Post save = postRepository.save(postMapper.mapDtoToPost(postRequest, authService.getCurrentUser(), subreddit));

        return postMapper.postToPostResponse(save);
    }


    @Transactional(readOnly = true)
    public List<PostResponse> getAllPosts() {
        return postRepository
                .findAll()
                .stream()
                .map(postMapper::postToPostResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PostResponse getPost(Long id) {

        Post post = postRepository
                .findById(id)
                .orElseThrow(() -> new SpringRedditException("Post Not found exception for id -  " + id));
        return postMapper.postToPostResponse(post);
    }

    @Transactional(readOnly = true)
    public List<PostResponse> getPostsBySubreddit(Long id) {

        Subreddit subreddit = subredditRepository.findById(id).orElseThrow(() -> new SpringRedditException("Subreddit Not found exception for id -  " + id));
        List<Post> posts = postRepository.findAllBySubreddit(subreddit);

        return posts.stream()
                .map(postMapper::postToPostResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PostResponse> getPostsByUsername(String username) {

        User user = userRepository.findByUsername(username).orElseThrow(() -> new SpringRedditException("User name Not found exception for id -  " + username));

       return postRepository.findByUser(user)
               .stream()
               .map(postMapper::postToPostResponse)
               .collect(Collectors.toList());

    }
}
