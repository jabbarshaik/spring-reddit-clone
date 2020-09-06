package com.sample.springredditclone.controller;


import com.sample.springredditclone.dto.AuthenticationResponse;
import com.sample.springredditclone.dto.LoginRequest;
import com.sample.springredditclone.dto.RefreshTokenRequest;
import com.sample.springredditclone.dto.RegisterRequest;
import com.sample.springredditclone.service.AuthService;
import com.sample.springredditclone.service.RefreshTokenService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.ResponseEntity.status;


@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;


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

    @PostMapping("/refresh/token")
    public ResponseEntity<AuthenticationResponse> refreshToken(@Valid  @RequestBody RefreshTokenRequest refreshTokenRequest){

        return status(HttpStatus.OK)
                .body(authService.refreshToken(refreshTokenRequest));
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
        refreshTokenService.deleteRefreshToken(refreshTokenRequest.getRefreshToken());
        return ResponseEntity.status(OK).body("Refresh Token Deleted Successfully!!");
    }
}
