package com.example.authentication.service;

import com.example.authentication.model.User;
import com.example.authentication.repository.UserRepo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;

@Service
public class AuthService {
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;

    private final String accessTokenSecret;

    private final String refreshTokenSecret;

    public AuthService(UserRepo userRepo, PasswordEncoder passwordEncoder, @Value("${application.security.access-token-secret}") String accessTokenSecret, @Value("${application.security.refresh-token-secret}") String refreshTokenSecret) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.accessTokenSecret = accessTokenSecret;
        this.refreshTokenSecret = refreshTokenSecret;
    }

    public User register(String firstName, String lastName, String email, String password, String passwordConfirm){
        if(!Objects.equals(password, passwordConfirm))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"password do not match");
        System.out.println(passwordEncoder.encode(password));
        return userRepo.save(User.of(firstName, lastName, email, passwordEncoder.encode(password)));
    }

    public Login login(String email, String password) {
        //find user by email
        var user  = userRepo.findByEmail(email).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,"email invalid credentials"));
        if(!passwordEncoder.matches(password, user.getPassword()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"password Invalid credentials");

        var login = Login.of(user.getId(),accessTokenSecret, refreshTokenSecret);
        var refreshJwt = login.getRefreshToken();

        user.addToken(new Token(refreshJwt.getToken(), refreshJwt.getIs))
        return Login.of(user.getId(), "very_long_and_secure_and_safe_secret_key","very_long_and_secure_and_safe_secret_key");

    }

    public Login refreshAccess(String refreshToken) {
        var userId = Token.from(refreshToken, refreshTokenSecret);

        return Login.of(userId, accessTokenSecret, Token.of(refreshToken));
    }
}
