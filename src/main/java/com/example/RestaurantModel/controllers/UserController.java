package com.example.RestaurantModel.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "user")
public class UserController {

    @GetMapping("/admin")
    public String sayHello() {
        return "Hello";
    }
}
