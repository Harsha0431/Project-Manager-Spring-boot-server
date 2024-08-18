package com.controller;

import com.ApiResponse.ApiResponse;
import com.manager.config.TokenService;
import com.model.*;
import com.service.UserDetailsService;
import com.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserDetailsController {
//    private static final List<String> educationType = new ArrayList<>(Arrays.asList("school", "under_graduation", "post_graduation"));
    @Autowired
    private TokenService tokenService;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private UserService userService;

    @PostMapping("/details/add")
    public ResponseEntity<ApiResponse<UserDetails>> addUserDetails(@RequestBody UserDetails details, HttpServletRequest request, HttpServletResponse res) {
        ApiResponse<UserDetails> response;
        try{
            User user = tokenService.getUserObjectFromHeaderToken(request, userService);
            if(user==null){
                return ResponseEntity.status(404).body(new ApiResponse<>(-1, "User not found", null));
            }
            details.setUser(user);
            response = userDetailsService.addUserDetails(details);
            return ResponseEntity.status(201).body(response);
        }
        catch (Exception e){
            return ResponseEntity.status(res.getStatus()).body(null);
        }

    }

    @PatchMapping("/details/update")
    public ResponseEntity<ApiResponse<UserDetails>> updateUserDetails(@RequestBody UserDetails details, HttpServletRequest request, HttpServletResponse res) {
        ApiResponse<UserDetails> response;
        try{
            if(details.getId()==null || details.getId() == 0){
                return ResponseEntity.status(400).body(new ApiResponse<>(-1, "User details not found", null));
            }
            User user = tokenService.getUserObjectFromHeaderToken(request, userService);
            if(user==null){
                return ResponseEntity.status(404).body(new ApiResponse<>(-1, "User not found", null));
            }
            details.setUser(user);
            response = userDetailsService.updateUserDetails(details);
            int status = response.getCode() == 1? 200 : 400;
            return ResponseEntity.status(status).body(response);
        }
        catch (Exception e){
            return ResponseEntity.status(res.getStatus()).body(null);
        }
    }

    @GetMapping("/details")
    public ResponseEntity<ApiResponse<UserDetails>> getUserDetails(HttpServletRequest request, HttpServletResponse response){
        try{
            String email = tokenService.getUserEmailFromToken(tokenService.getTokenFromRequest(request));
            ApiResponse<UserDetails> res = userDetailsService.getUserDetails(email);
            int status = res.getCode() == 1 ? 200 : 400;
            return ResponseEntity.status(status).body(res);
        }
        catch (Exception e){
            System.out.println("Caught exception getUserDetails in controller: " + e.getMessage());
            return ResponseEntity.status(response.getStatus()).body(new ApiResponse<>(-1, "Failed to get user details.", null));
        }
    }
}
