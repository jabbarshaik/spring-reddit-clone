package com.sample.springredditclone.repository;


import com.sample.springredditclone.model.Comment;
import com.sample.springredditclone.model.Post;
import com.sample.springredditclone.model.User;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByPost(Post post);

    List<Comment> findAllByUser(User user);
}