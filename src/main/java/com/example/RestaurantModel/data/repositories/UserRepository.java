package com.example.RestaurantModel.data.repositories;

import com.example.RestaurantModel.data.models.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public interface UserRepository extends JpaRepository<UserEntity, UUID> {
    UserEntity findByName(String name);
    UserEntity findFirstByName(String name);

}
