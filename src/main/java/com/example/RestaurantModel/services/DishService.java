package com.example.RestaurantModel.services;

import com.example.RestaurantModel.data.models.Dish;
import com.example.RestaurantModel.data.models.Order;
import com.example.RestaurantModel.data.repositories.DishRepository;
import com.example.RestaurantModel.data.repositories.OrderRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class DishService {
    @Autowired
    private DishRepository dishRepository;
    @Autowired
    private OrderRepository orderRepository;

    public Dish addDish(Dish dish) {
        dish.setSold(0);
        dish.setAvgEstimation(0D);
        return dishRepository.save(dish);
    }

    public Boolean deleteDish(UUID id) {
        dishRepository.deleteById(id);
        return true;
    }

    public Dish updateDish(UUID id, Dish updatedDish) {
        Dish dish = dishRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Dish not found with id: " + id));
        if (updatedDish.getName() != null) {
            dish.setName(updatedDish.getName());
        }
        if (updatedDish.getCookingTime() != 0) {
            dish.setCookingTime(updatedDish.getCookingTime());
        }
        if (updatedDish.getPrice() != 0) {
            dish.setPrice(updatedDish.getPrice());
        }
        if (updatedDish.getCount() != 0) {
            dish.setCount(updatedDish.getCount());
        }
        return dishRepository.save(dish);
    }

    public List<Dish> getAllDishes() {
        return dishRepository.findAll();
    }

    public Dish getDishById(UUID id) {
        return dishRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Dish not found with id: " + id));
    }

    public List<Dish> getDishesByName(List<String> dishesName) {
        List<Dish> dishes = new ArrayList<>();
        for (String name : dishesName) {
            Optional<Dish> dish = dishRepository.findByName(name);
            if (dish.isPresent()) {
                dishes.add(dish.get());
            }
        }
        return dishes;
    }

    public List<Dish> getRemainingDishes(UUID order_id) {
        Optional<Order> optionalOrder = orderRepository.findById(order_id);
        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();
            List<Dish> curDish = order.getDishes();
            List<Dish> allDish = dishRepository.findAll();
            List<Dish> dishes = new ArrayList<>();
            for (Dish dish : allDish) {
                if (!curDish.contains(dish)) {
                    dishes.add(dish);
                }
            }
            return dishes;
        }
        throw new EntityNotFoundException("no such order with this id");
    }

    public void save(Dish dish) {
        try {
            dishRepository.save(dish);
        } catch (Exception e) {

        }
    }

    public void addFeedback(UUID id, int number, String text) {
        Optional<Dish> OptionalDish = dishRepository.findById(id);
        if (OptionalDish.isPresent()) {
            Dish dish = OptionalDish.get();
            List<String> feedback = dish.getFeedback();
            int sold = dish.getSold();
            double avg = dish.getAvgEstimation();
            feedback.add(text);
            if (avg == 0D) {
                avg = number;
            } else {
                avg = (avg + number) / 2D;
            }
            dish.setFeedback(feedback);
            dish.setAvgEstimation(avg);
            dishRepository.save(dish);
        }
    }
}
