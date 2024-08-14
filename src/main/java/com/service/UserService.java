package com.service;

import com.ApiResponse.ApiResponse;
import com.manager.config.TokenService;
import com.model.User;
import com.repository.UserCustomRepository;
import com.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;


@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserCustomRepository userCustomRepository;
    @Autowired
    private TokenService tokenService;

    public ApiResponse<User> saveUser(User user){
        try{
            if(userRepository.existsById(user.getEmail())){
                return new ApiResponse<>(0, "User with given email already exists", null);
            }
            // return userCustomRepository.addUser(user);  # Using custom repository
            User savedObject = userRepository.save(user);
             return new ApiResponse<>(1, "User created successfully", savedObject);
        } catch(Exception e){
            System.out.println("Caught exception in saveUser service: " + e.getMessage());
            if(e.getMessage().contains("user.uk_user"))
                return new ApiResponse<>(0, "Username already exists", null);
            return new ApiResponse<>(-1, "Failed to register user", null);
        }
    }

    public ApiResponse<Map<String, String>> verifyUserCredentials(String email, String password){
        try{
            User user = userRepository.findById(email).orElse(null);
            if(user == null){
                return new ApiResponse<>(0, "User with email " + email.strip() + " not found.", null);
            }
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            if(!encoder.matches(password, user.getPassword())){
                return new ApiResponse<>(0, "Invalid password", null);
            }
            String token = tokenService.generateJwtToken(user.getEmail());
            Map<String, String> data = new HashMap<>();
            data.put("token", token);
            return new ApiResponse<>(1, "Login successful", data);
        }
        catch (Exception e){
            System.out.println("Caught exception in verifyUserCredentials service: " + e.getMessage());
            return new ApiResponse<>(-1, "Failed to verify user credentials", null);
        }
    }

    public ApiResponse<String> verifyUserToken(HttpServletRequest request){
        try{
            String token = tokenService.getTokenFromRequest(request);
            if(token!=null && !token.isEmpty()){
                boolean isTokenValid = tokenService.isTokenValid(token);
                if(isTokenValid) {
                    String payloadEmail = tokenService.getUserEmailFromToken(token);
                    return new ApiResponse<>(1, "Login successful.", payloadEmail);
                }
            }
            return new ApiResponse<>(0, "Invalid token or no token provided. Please login to continue", null);
        }
        catch (Exception e){
            System.out.println("Caught exception in verifyUserToken in UserService due to ~ " + e.getMessage());
            return new ApiResponse<>(-1, "", null);
        }
    }


}
