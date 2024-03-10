package com.example.RestaurantModel.data.repositories;

import com.example.RestaurantModel.data.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Role findByName(String name);
}
