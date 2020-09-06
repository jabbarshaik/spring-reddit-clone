package com.sample.springredditclone.repository;

import com.sample.springredditclone.model.Post;
import com.sample.springredditclone.model.User;
import com.sample.springredditclone.model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VoteRepository extends JpaRepository<Vote, Long> {
    Optional<Vote> findTopByPostAndUserOrderByVoteIdDesc(Post post, User currentUser);
}
