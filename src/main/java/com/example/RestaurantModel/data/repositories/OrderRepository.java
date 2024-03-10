package com.example.RestaurantModel.data.repositories;

import com.example.RestaurantModel.data.models.Order;
import com.example.RestaurantModel.data.models.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, UUID> {
}
