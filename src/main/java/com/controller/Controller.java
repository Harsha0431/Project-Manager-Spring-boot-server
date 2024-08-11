package com.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {
    @PostMapping(path = "/test")
    public String getHome(){
        return "Hello World";
    }
}
