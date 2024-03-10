package com.example.RestaurantModel.data.repositories;

import com.example.RestaurantModel.data.models.Dish;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface DishRepository extends JpaRepository<Dish, UUID> {
    Optional<Dish> findByName(String name);
}
