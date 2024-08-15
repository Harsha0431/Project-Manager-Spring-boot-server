package com.controller;

import com.manager.config.TokenService;
import com.model.SchoolEducation;
import com.model.UnderGraduation;
import com.model.User;
import com.model.UserEducation;
import com.repository.Education;
import com.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class Controller {
    @Autowired
    private Education repo;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private UserRepository userRepository;
    @PostMapping(path = "/test")
    public UserEducation getHome(@RequestBody UnderGraduation s, HttpServletRequest req){
        String email = tokenService.getUserEmailFromToken(tokenService.getTokenFromRequest(req));
        s.setUser(userRepository.findById(email).get());
        return repo.save(s);
    }
}
