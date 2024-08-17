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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user/education")
public class EducationController {
    @Autowired
    private TokenService tokenService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserDetailsService userDetailsService;

    @PostMapping("/school/add")
    public ResponseEntity<ApiResponse<UserEducation>> addUserSchoolEducation(@RequestBody SchoolEducation education, HttpServletRequest request, HttpServletResponse httpResponse){
        ApiResponse<UserEducation> response;
        String type = "school";
        try{
            User user = tokenService.getUserObjectFromHeaderToken(request, userService);
            education.setUser(user);
            response = userDetailsService.addUserEducation(education, type);
            int status = response.getCode() == 1 ? 200 : 400;
            return ResponseEntity.status(status).body(response);
        }
        catch(Exception e){
            System.out.println("Caught exception in addUserSchoolEducation controller : " + e.getMessage());
            return ResponseEntity.status(httpResponse.getStatus()).body(new ApiResponse<>(-1, "Failed to save education details.", null));
        }
    }

    @PostMapping("/ug/add")
    public ResponseEntity<ApiResponse<UserEducation>> addUserUnderGraduationEducation(@RequestBody UnderGraduation education, HttpServletRequest request, HttpServletResponse httpResponse){
        ApiResponse<UserEducation> response;
        String type = "under_graduation";
        try{
            User user = tokenService.getUserObjectFromHeaderToken(request, userService);
            education.setUser(user);
            response = userDetailsService.addUserEducation(education, type);
            int status = response.getCode() == 1 ? 200 : 400;
            return ResponseEntity.status(status).body(response);
        }
        catch(Exception e){
            System.out.println("Caught exception in addUserUnderGraduationEducation controller : " + e.getMessage());
            return ResponseEntity.status(httpResponse.getStatus()).body(new ApiResponse<>(-1, "Failed to save education details.", null));
        }
    }

    @PostMapping("/pg/add")
    public ResponseEntity<ApiResponse<UserEducation>> addUserPostGraduationEducation(@RequestBody PostGraduation education, HttpServletRequest request, HttpServletResponse httpResponse){
        ApiResponse<UserEducation> response;
        String type = "post_graduation";
        try{
            User user = tokenService.getUserObjectFromHeaderToken(request, userService);
            education.setUser(user);
            response = userDetailsService.addUserEducation(education, type);
            int status = response.getCode() == 1 ? 200 : 400;
            return ResponseEntity.status(status).body(response);
        }
        catch(Exception e){
            System.out.println("Caught exception in addUserUnderGraduationEducation controller : " + e.getMessage());
            return ResponseEntity.status(httpResponse.getStatus()).body(new ApiResponse<>(-1, "Failed to save education details.", null));
        }
    }

}
