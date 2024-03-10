package com.example.RestaurantModel.services;

import com.example.RestaurantModel.data.models.Order;
import com.example.RestaurantModel.data.models.UserEntity;
import com.example.RestaurantModel.data.models.Role;
import com.example.RestaurantModel.data.repositories.OrderRepository;
import com.example.RestaurantModel.data.repositories.RoleRepository;
import com.example.RestaurantModel.data.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Collections;
import java.util.List;
import java.util.UUID;


@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder encoder;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByName(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        return user;
    }

    public boolean addUser(UserEntity user) {
        UserEntity newUser = userRepository.findByName(user.getUsername());
        if (newUser != null) {
            return false;
        }
        newUser = new UserEntity();
        newUser.setRole(Collections.singleton(roleRepository.findByName("ROLE_USER")));
        newUser.setName(user.getName());
        newUser.setPassword(encoder.encode(user.getPassword()));
        userRepository.save(newUser);
        return true;
    }

    public List<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }

    public void deleteUser(UUID id) {
        userRepository.deleteById(id);
    }

    public UserEntity getUserByName(String name) {
        UserEntity user = userRepository.findByName(name);
        if (user == null) {
            throw new UsernameNotFoundException("User was not found");
        }
        return user;
    }

    public boolean isUserExist(UserEntity userIn) {
        UserEntity user = userRepository.findFirstByName(userIn.getName());
        return user != null;
    }



}
