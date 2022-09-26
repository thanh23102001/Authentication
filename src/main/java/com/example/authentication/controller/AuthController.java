package com.example.authentication.controller;

import com.example.authentication.model.User;
import com.example.authentication.repository.UserRepo;
import com.example.authentication.service.AuthService;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

@RestController
@RequestMapping(value = "/api")
public class AuthController {

    private final UserRepo userRepo;

    @Autowired
    private  AuthService authService;

    public AuthController(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @GetMapping(value = "/hello")
    public String hello(){
        return "Hello!";
    }

    record RegisterRequest(@JsonProperty("first_name") String firstName,
                           @JsonProperty("last_name") String lastName,
                           String email,
                           String password,
                           @JsonProperty("password_confirm") String passwordConfirm){

    }
    record RegisterResponse(Long id,
                           @JsonProperty("first_name") String firstName,
                           @JsonProperty("last_name") String lastName,
                           String email){
}

    @PostMapping(value ="/register")
    public RegisterResponse register(@RequestBody RegisterRequest registerRequest){
        if(!Objects.equals(registerRequest.password(), registerRequest.passwordConfirm()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"password do not match");


        var user = authService.register(registerRequest.firstName(),
                registerRequest.lastName(),
                registerRequest.email(),
                registerRequest.password(),
                registerRequest.passwordConfirm());

        return new RegisterResponse(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail());
    }

    record LoginRquest(String email, String password){}

    record LoginResponse(String token){}

    @PostMapping(value="/login")
    public LoginResponse login(@RequestBody LoginRquest loginRquest, HttpServletResponse response){
        var login = authService.login(loginRquest.email(), loginRquest.password());
        Cookie cookie = new Cookie("refresh_token", login.getRefreshToken().getToken());
        cookie.setMaxAge(3600);
        cookie.setHttpOnly(true);
        cookie.setPath("/api");

        response.addCookie(cookie);
        return  new LoginResponse(login.getAccessToken().getToken());
    }

    record UserResponse(Long id,
                            @JsonProperty("first_name") String firstName,
                            @JsonProperty("last_name") String lastName,
                            String email){}

    @GetMapping(value = "/user")
    public UserResponse user(HttpServletRequest request){
         var user = (User) request.getAttribute("user");
         return  new UserResponse(user.getId(), user.getFirstName(),user.getLastName(), user.getEmail());
    }

    record RefreshResponse(String token){
    }
    @PostMapping(value = "/refresh")
    public RefreshResponse refresh(@CookieValue("refresh_token") String refreshToken){
          return new RefreshResponse(authService.refreshAccess(refreshToken).getAccessToken().getToken());
    }

    record LogoutResponse(String message){
    }

    @PostMapping(value = "/logout")
    public LogoutResponse logout(HttpServletResponse response){
        Cookie cookie = new Cookie("refresh_token",null);
        cookie.setMaxAge(0);
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
        return new LogoutResponse("success");
    }

}
