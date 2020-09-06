package com.sample.springredditclone.service;


import com.sample.springredditclone.dto.AuthenticationResponse;
import com.sample.springredditclone.dto.LoginRequest;
import com.sample.springredditclone.dto.RegisterRequest;
import com.sample.springredditclone.exception.SpringRedditException;
import com.sample.springredditclone.model.NotificationEmail;
import com.sample.springredditclone.model.User;
import com.sample.springredditclone.model.VerificationToken;
import com.sample.springredditclone.repository.UserRepository;
import com.sample.springredditclone.repository.VerificationTokenRepository;
import com.sample.springredditclone.security.JWTProvider;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor

public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final MailService mailService;

    private final AuthenticationManager authenticationManager;
    private final JWTProvider jwtProvider;


    @Transactional
    public void signUp(RegisterRequest registerRequest) {

        User user = User.builder()
                .username(registerRequest.getUserName())
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .created(Instant.now())
                .enabled(false).build();


        userRepository.save(user);


        String token = generationVerificationToken(user);
        mailService.sendEmail(new NotificationEmail("Please Activate your Account",
                user.getEmail(), "Thank you for signing up to Spring Reddit, " +
                "please click on the below url to activate your account : " +
                "http://localhost:8080/api/auth/accountVerification/" + token));

    }

    private String generationVerificationToken(User user) {

        String token = UUID.randomUUID().toString();

        VerificationToken verificationToken = VerificationToken.builder()
                .token(token)
                .user(user).build();
        verificationTokenRepository.save(verificationToken);

        return token;
    }

    public void verifyToken(String token) {
        Optional<VerificationToken> byToken = verificationTokenRepository.findByToken(token);
        byToken.orElseThrow(() -> new SpringRedditException("Invalid Token"));

        fetchUserAndEnable(byToken.get());

    }

    @Transactional
    public void fetchUserAndEnable(VerificationToken verificationToken) {
        String username = verificationToken.getUser().getUsername();
        Optional<User> byUsername = userRepository.findByUsername(username);
        User user = byUsername.orElseThrow(() -> new SpringRedditException("User does not exist"));

        user.setEnabled(true);
        userRepository.save(user);
    }

    public AuthenticationResponse login(LoginRequest loginRequest) {

        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authenticate);

        String token = jwtProvider.generateToken(authenticate);
        return new AuthenticationResponse(token, loginRequest.getUsername());
    }

    public User getCurrentUser() {
        org.springframework.security.core.userdetails.User principal = (org.springframework.security.core.userdetails.User)
                SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return userRepository.findByUsername(principal.getUsername()).orElseThrow(() ->  new UsernameNotFoundException("User name not found :::  " + principal.getUsername()));
    }
}
