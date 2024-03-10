package com.example.RestaurantModel.services;

import com.example.RestaurantModel.data.enums.Status;
import com.example.RestaurantModel.data.forms.OrderForm;
import com.example.RestaurantModel.data.models.Dish;
import com.example.RestaurantModel.data.models.Order;
import com.example.RestaurantModel.data.models.UserEntity;
import com.example.RestaurantModel.data.repositories.DishRepository;
import com.example.RestaurantModel.data.repositories.OrderRepository;
import com.example.RestaurantModel.data.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private DishService dishService;

    public boolean createOrder(UserEntity user, OrderForm orderForm) {
        Order order = new Order();
        order.setUserId(user);
        List<Dish> dishes = dishService.getDishesByName(orderForm.getSelectedDishes());
        for (Dish dish : dishes) {
            if (dish.getCount() <= 0) {
                throw new RuntimeException("out of stock");
            }
            else {
                dish.setCount(dish.getCount() - 1);
                dish.setSold(dish.getSold() + 1);
                dishService.save(dish);
            }
        }
        order.setDishes(dishes);
        order.setStatus(Status.ACCEPTED);
        order.setTime(LocalDateTime.now());
        orderRepository.save(order);
        cookOrderAsync(order);
        return true;
    }

    public void cookOrderAsync(Order order) {
        CompletableFuture.runAsync(() -> {
            int maxCookingTime = order.getDishes().stream()
                    .mapToInt(Dish::getCookingTime)
                    .max()
                    .orElse(0);
            try {
                Thread.sleep(maxCookingTime * 1000);
                order.setStatus(Status.READY);
                orderRepository.save(order);
            } catch (InterruptedException e) {
            }
        });
        order.setStatus(Status.READY);
    }

    public void cancelOrder(Order order) {
        if (order.getStatus() == Status.PREPARING) {
            orderRepository.deleteById(order.getId());
        }
    }

    public List<Order> getAllOrdersByUsername(String name) {
        List<Order> orders = orderRepository.findAll();
        List<Order> ordersByUser = new ArrayList<>();
        UserEntity user = userRepository.findByName(name);
        for (Order order : orders) {
            if (order.getUserId().getId() == user.getId()) {
                ordersByUser.add(order);
            }
        }
        return ordersByUser;
    }

    public void deleteOrder(UUID id) {
        orderRepository.deleteById(id);
    }

    public Order getOrder(UUID id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Dish not found with id: " + id));
    }

    public void deleteDish(UUID id, UUID dish_id) {
        Optional<Order> optionalOrder = orderRepository.findById(id);
        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();
            order.getDishes().removeIf(dish -> dish.getId().equals(dish_id));
            orderRepository.save(order);
        } else {
            throw new IllegalArgumentException("Order not found");
        }
    }

    public void pay(UUID id){
        UserEntity user = userRepository.findByName("admin");
        int sum = user.getSum();
        Order order = getOrder(id);
        List<Dish> dishes = order.getDishes();
        sum += dishes.stream().mapToInt(Dish::getPrice).sum();
        user.setSum(sum);
        userRepository.save(user);
    }

    public void addDishes(UUID id, OrderForm orderForm) throws Exception {
        List<Dish> addingDishes = dishService.getDishesByName(orderForm.getSelectedDishes());
        Optional<Order> optionalOrder = orderRepository.findById(id);
        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();
            List<Dish> currDishes = order.getDishes();
            for (Dish dish: addingDishes) {
                int count = dish.getCount();
                dish.setCount(count - 1);
                dishService.save(dish);
            }
            currDishes.addAll(addingDishes);
            order.setDishes(currDishes);
            orderRepository.save(order);
        }
    }

}
