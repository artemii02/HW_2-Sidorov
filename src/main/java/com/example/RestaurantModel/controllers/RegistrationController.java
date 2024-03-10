package com.example.RestaurantModel.controllers;

import com.example.RestaurantModel.data.models.UserEntity;
import com.example.RestaurantModel.data.repositories.RoleRepository;
import com.example.RestaurantModel.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.security.sasl.AuthenticationException;
import javax.validation.Valid;
import java.util.Collections;
import java.util.Objects;

@Controller
@RequestMapping
public class RegistrationController {
    @Autowired
    private UserService userService;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder encoder;


    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("userEntity", new UserEntity());
        return "security/login";
    }

    @PostMapping("/login")
    public String authentication(@ModelAttribute("userEntity") UserEntity user) throws AuthenticationException {
        UserEntity existingUser = userService.getUserByName(user.getName());
        if (existingUser == null) {
            throw new AuthenticationException("No such user");
        }
        if (!encoder.matches(user.getPassword(), existingUser.getPassword())) {
            throw new AuthenticationException("Invalid password");
        }
        return "redirect:/admin/menu";
    }


    @GetMapping("/registration")
    public String registration(Model model) {
        model.addAttribute("userEntity", new UserEntity());

        return "security/registration";
    }

    @PostMapping("/registration")
    public String addUser(@ModelAttribute("userEntity") UserEntity user) {
        userService.addUser(user);

        return "security/login";
    }

    @RequestMapping("/index")
    public String defaultAfterLogin(HttpServletRequest httpServletRequest) throws Exception {
        if (httpServletRequest.isUserInRole("ROLE_ADMIN")) {
            return "redirect:/admin/menu";
        }
        if (httpServletRequest.isUserInRole("ROLE_USER")) {
            return "redirect:/visitor/menu";
        }
        throw new Exception("smth went wrong");
    }
}
