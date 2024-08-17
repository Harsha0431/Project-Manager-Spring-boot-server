package com.controller;

import com.ApiResponse.ApiResponse;
import com.model.UserEducation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class Controller {
    @PostMapping(path = "/test")
    public ApiResponse<UserEducation> getHome(){
        return new ApiResponse<>(-1, null, null);
    }
}
