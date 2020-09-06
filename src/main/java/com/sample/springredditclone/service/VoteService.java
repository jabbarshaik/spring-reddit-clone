package com.sample.springredditclone.service;

import com.sample.springredditclone.dto.VoteDto;
import com.sample.springredditclone.exception.SpringRedditException;
import com.sample.springredditclone.model.Post;
import com.sample.springredditclone.model.Vote;
import com.sample.springredditclone.repository.PostRepository;
import com.sample.springredditclone.repository.VoteRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.sample.springredditclone.model.VoteType.UPVOTE;


@Service
@AllArgsConstructor
public class VoteService {

    private final VoteRepository voteRepository;
    private final PostRepository postRepository;
    private final AuthService authService;

    @Transactional
    public void vote(VoteDto voteDto) {
        Post post = postRepository.findById(voteDto.getPostId())
                .orElseThrow(() -> new SpringRedditException("Post Not Found with ID - " + voteDto.getPostId()));
        Optional<Vote> voteByPostAndUser = voteRepository.findTopByPostAndUserOrderByVoteIdDesc(post, authService.getCurrentUser());
        if (voteByPostAndUser.isPresent() &&
                voteByPostAndUser.get().getVoteType()
                        .equals(voteDto.getVoteType())) {
            throw new SpringRedditException("You have already "
                    + voteDto.getVoteType() + "'d for this post");
        }
        if (UPVOTE.equals(voteDto.getVoteType())) {
            if(post.getVoteCount() == null){
                post.setVoteCount(1);
            }else{
                post.setVoteCount(post.getVoteCount() + 1);
            }

        } else {
            if(post.getVoteCount() == null){
                post.setVoteCount(1);
            }else{
                post.setVoteCount(post.getVoteCount() -1 );
            }
        }
        voteRepository.save(mapToVote(voteDto, post));
        postRepository.save(post);
    }

    private Vote mapToVote(VoteDto voteDto, Post post) {
        return Vote.builder()
                .voteType(voteDto.getVoteType())
                .post(post)
                .user(authService.getCurrentUser())
                .build();
    }

}