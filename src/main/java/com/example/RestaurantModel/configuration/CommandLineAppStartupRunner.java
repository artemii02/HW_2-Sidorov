package com.example.RestaurantModel.configuration;

import com.example.RestaurantModel.data.models.UserEntity;
import com.example.RestaurantModel.data.models.Role;
import com.example.RestaurantModel.data.repositories.RoleRepository;
import com.example.RestaurantModel.data.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class CommandLineAppStartupRunner implements CommandLineRunner {
    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Override
    public void run(String...args) throws Exception {
        if (roleRepository.findByName("ROLE_ADMIN") == null) {
            Role role = new Role(0, "ROLE_ADMIN");
            roleRepository.save(role);
        }
        if (roleRepository.findByName("ROLE_USER") == null) {
            Role role = new Role(1, "ROLE_USER");
            roleRepository.save(role);
        }
        if (userRepository.findByName("admin") == null) {
            UserEntity admin = new UserEntity();
            admin.setName("admin");
            admin.setPassword(encoder.encode("admin"));
            admin.setRole(Collections.singleton(roleRepository.findByName("ROLE_ADMIN")));
            int sum = 0;
            admin.setSum(sum);
            userRepository.save(admin);
        }
        if (userRepository.findByName("user") == null) {
            UserEntity visitor = new UserEntity();
            visitor.setName("user");
            visitor.setPassword(encoder.encode("user"));
            visitor.setRole(Collections.singleton(roleRepository.findByName("ROLE_USER")));
            userRepository.save(visitor);
        }
    }
}