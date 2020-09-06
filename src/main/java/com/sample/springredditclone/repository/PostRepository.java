package com.sample.springredditclone.repository;
import com.sample.springredditclone.model.Post;
import com.sample.springredditclone.model.Subreddit;
import com.sample.springredditclone.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findAllBySubreddit(Subreddit subreddit);

    List<Post> findByUser(User user);
}
