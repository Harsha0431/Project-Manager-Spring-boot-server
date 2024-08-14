package com.controller;

import com.ApiResponse.ApiResponse;
import com.model.User;
import com.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.net.http.HttpRequest;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ApiResponse<User> addUser(@RequestBody User user) {
        return userService.saveUser(user);
    }

    private static class UserCredentials{
        String email;
        String password;

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

    @PostMapping("/login")
    public ApiResponse<Map<String, String>> verifyUserCredentials(@RequestBody UserCredentials credentials){
        return userService.verifyUserCredentials(credentials.getEmail(), credentials.getPassword());
    }

    @GetMapping("/verify")
    public ApiResponse<String> verifyUserToken(HttpServletRequest request){
        return userService.verifyUserToken(request);
    }

}
