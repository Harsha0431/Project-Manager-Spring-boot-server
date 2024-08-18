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

import java.util.List;

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
            response = userDetailsService.addUserEducation(education, type, "add");
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
            response = userDetailsService.addUserEducation(education, type, "add");
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
            response = userDetailsService.addUserEducation(education, type, "add");
            int status = response.getCode() == 1 ? 200 : 400;
            return ResponseEntity.status(status).body(response);
        }
        catch(Exception e){
            System.out.println("Caught exception in addUserUnderGraduationEducation controller : " + e.getMessage());
            return ResponseEntity.status(httpResponse.getStatus()).body(new ApiResponse<>(-1, "Failed to save education details.", null));
        }
    }

    // Update
    @PatchMapping("/school")
    public ResponseEntity<ApiResponse<UserEducation>> updateUserSchoolEducation(@RequestBody SchoolEducation education, HttpServletRequest request, HttpServletResponse httpResponse){
        ApiResponse<UserEducation> response;
        String type = "school";
        try{
            User user = tokenService.getUserObjectFromHeaderToken(request, userService);
            education.setUser(user);
            response = userDetailsService.addUserEducation(education, type, "update");
            int status = response.getCode() == 1 ? 200 : 400;
            return ResponseEntity.status(status).body(response);
        }
        catch(Exception e){
            System.out.println("Caught exception in updateUserSchoolEducation controller : " + e.getMessage());
            return ResponseEntity.status(httpResponse.getStatus()).body(new ApiResponse<>(-1, "Failed to update education details.", null));
        }
    }

    @PatchMapping("/ug")
    public ResponseEntity<ApiResponse<UserEducation>> updateUserUnderGraduationEducation(@RequestBody UnderGraduation education, HttpServletRequest request, HttpServletResponse httpResponse){
        ApiResponse<UserEducation> response;
        String type = "under_graduation";
        try{
            User user = tokenService.getUserObjectFromHeaderToken(request, userService);
            education.setUser(user);
            response = userDetailsService.addUserEducation(education, type, "update");
            int status = response.getCode() == 1 ? 200 : 400;
            return ResponseEntity.status(status).body(response);
        }
        catch(Exception e){
            System.out.println("Caught exception in updateUserUnderGraduationEducation controller : " + e.getMessage());
            return ResponseEntity.status(httpResponse.getStatus()).body(new ApiResponse<>(-1, "Failed to update education details.", null));
        }
    }

    @PatchMapping("/pg")
    public ResponseEntity<ApiResponse<UserEducation>> updateUserPostGraduationEducation(@RequestBody PostGraduation education, HttpServletRequest request, HttpServletResponse httpResponse){
        ApiResponse<UserEducation> response;
        String type = "post_graduation";
        try{
            User user = tokenService.getUserObjectFromHeaderToken(request, userService);
            education.setUser(user);
            response = userDetailsService.addUserEducation(education, type, "update");
            int status = response.getCode() == 1 ? 200 : 400;
            return ResponseEntity.status(status).body(response);
        }
        catch(Exception e){
            System.out.println("Caught exception in updateUserUnderGraduationEducation controller : " + e.getMessage());
            return ResponseEntity.status(httpResponse.getStatus()).body(new ApiResponse<>(-1, "Failed to update education details.", null));
        }
    }

    // Delete
    @DeleteMapping("")
    public ResponseEntity<ApiResponse<String>> deleteUserEducation(@RequestParam Long id, HttpServletResponse response, HttpServletRequest request){
        try{
            String email = tokenService.getUserEmailFromToken(tokenService.getTokenFromRequest(request));
            ApiResponse<String> res = userDetailsService.deleteEducationEntityWithIdAndEmail(id, email);
            int status = res.getCode() == 1 ? 200 : 400;
            return ResponseEntity.status(status).body(res);
        }
        catch (Exception e){
            System.out.println("Caught exception in deleteUserEducation controller: " + e.getMessage());
            return ResponseEntity.status(response.getStatus()).body(new ApiResponse<>(-1, "Failed to delete education details.", null));
        }
    }

    @GetMapping("")
    public ResponseEntity<ApiResponse<List<UserEducation>>> getUserEducationList(HttpServletResponse response, HttpServletRequest request){
        try{
            User user = tokenService.getUserObjectFromHeaderToken(request, userService);
            ApiResponse<List<UserEducation>> res = userDetailsService.getUserEducationList(user);
            int status = res.getCode() == 1 ? 200 : 400;
            return ResponseEntity.status(status).body(res);
        }
        catch (Exception e){
            System.out.println("Caught exception in getUserEducationList controller: " + e.getMessage());
            return ResponseEntity.status(response.getStatus()).body(new ApiResponse<>(-1, "Failed to fetched related education details.", null));
        }
    }


}
