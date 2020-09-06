package com.sample.springredditclone.service;


import com.sample.springredditclone.dto.CommentsDto;
import com.sample.springredditclone.exception.SpringRedditException;
import com.sample.springredditclone.mapper.CommentMapper;
import com.sample.springredditclone.model.Comment;
import com.sample.springredditclone.model.NotificationEmail;
import com.sample.springredditclone.model.Post;
import com.sample.springredditclone.model.User;
import com.sample.springredditclone.repository.CommentRepository;
import com.sample.springredditclone.repository.PostRepository;
import com.sample.springredditclone.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
public class CommentService {

    private static final String POST_URL = "";
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final AuthService authService;
    private final CommentMapper commentMapper;
    private final CommentRepository commentRepository;
    private final MailContentBuilder mailContentBuilder;
    private final MailService mailService;


    @Transactional
    public CommentsDto save(CommentsDto commentsDto) {

        Post post = postRepository.findById(commentsDto.getPostId())
                .orElseThrow(() -> new SpringRedditException("Post id not found for the post id - " + commentsDto.getPostId().toString()));
        Comment comment = commentMapper.map(commentsDto, post, authService.getCurrentUser());
        Comment savedComment = commentRepository.save(comment);

        String message = mailContentBuilder.build(authService.getCurrentUser() + " posted a comment on your post." + POST_URL);
        sendCommentNotification(message, post.getUser());

        return commentMapper.mapToDto(savedComment);
    }

    private void sendCommentNotification(String message, User user) {
        mailService.sendEmail(new NotificationEmail(user.getUsername() + " Commented on your post", user.getEmail(), message));
    }

    public List<CommentsDto> getAllCommentsForPost(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new SpringRedditException(postId.toString()));
        return commentRepository.findByPost(post)
                .stream()
                .map(commentMapper::mapToDto).collect(toList());
    }

    public List<CommentsDto> getAllCommentsForUser(String userName) {
        User user = userRepository.findByUsername(userName)
                .orElseThrow(() -> new UsernameNotFoundException(userName));
        return commentRepository.findAllByUser(user)
                .stream()
                .map(commentMapper::mapToDto)
                .collect(toList());
    }

}
