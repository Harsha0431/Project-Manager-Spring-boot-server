package com.service;

import com.ApiResponse.ApiResponse;
import com.model.User;
import com.repository.UserCustomRepository;
import com.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserCustomRepository userCustomRepository;

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

    public ApiResponse<String> verifyUserCredentials(String email, String password){
        try{
            User user = userRepository.findById(email).orElse(null);
            if(user == null){
                return new ApiResponse<String>(0, "User with email " + email.strip() + " not found.", null);
            }
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            if(!encoder.matches(password, user.getPassword())){
                return new ApiResponse<String>(0, "Invalid password", null);
            }
            return new ApiResponse<String>(1, "Login successful", null);
        }
        catch (Exception e){
            System.out.println("Caught exception in verifyUserCredentials service: " + e.getMessage());
            return new ApiResponse<String>(-1, "Failed to verify user credentials", null);
        }
    }
}
