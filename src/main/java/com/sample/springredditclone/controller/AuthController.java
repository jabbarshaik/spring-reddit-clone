package com.sample.springredditclone.controller;


import com.sample.springredditclone.dto.AuthenticationResponse;
import com.sample.springredditclone.dto.LoginRequest;
import com.sample.springredditclone.dto.RegisterRequest;
import com.sample.springredditclone.service.AuthService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody RegisterRequest registerRequest){
       authService.signUp(registerRequest);
        return new ResponseEntity<>("User Registration Successful", OK);

    }

    @GetMapping("/accountVerification/{token}")
    public ResponseEntity<String> verifyToken(@PathVariable String token){
        authService.verifyToken(token);

        return new ResponseEntity<>("Account activated successfully", OK);
    }

    @PostMapping("/login")
    public AuthenticationResponse login(@RequestBody LoginRequest loginRequest){
        return  authService.login(loginRequest);

    }

}
