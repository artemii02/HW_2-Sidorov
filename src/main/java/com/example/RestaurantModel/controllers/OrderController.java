package com.example.RestaurantModel.controllers;

import com.example.RestaurantModel.data.enums.Status;
import com.example.RestaurantModel.data.forms.OrderForm;
import com.example.RestaurantModel.data.models.Dish;
import com.example.RestaurantModel.data.models.Order;
import com.example.RestaurantModel.data.models.UserEntity;
import com.example.RestaurantModel.data.repositories.DishRepository;
import com.example.RestaurantModel.services.DishService;
import com.example.RestaurantModel.services.OrderService;
import com.example.RestaurantModel.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Controller
@RequestMapping
public class OrderController {

    @Autowired
    private DishService dishService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private UserService userService;

    @GetMapping("/visitor/menu")
    public String showMenu(Model model) {
        List<Dish> dishes = dishService.getAllDishes();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        model.addAttribute("dishes", dishes);
        model.addAttribute("orderForm", new OrderForm());
        List<Order> orders = orderService.getAllOrdersByUsername(username);
        model.addAttribute("orders", orders);
        model.addAttribute("ready", Status.READY);
        return "visitor/menu";
    }

    @PostMapping("visitor/make_order")
    public String makeOrder(@ModelAttribute("orderForm") OrderForm orderForm, Principal principal) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        orderService.createOrder(userService.getUserByName(username), orderForm);
        return "redirect:/visitor/menu";
    }

    @DeleteMapping("visitor/delete_order/{id}")
    public String deleteOrder(@PathVariable("id") UUID id) throws Exception {
        orderService.deleteOrder(id);
        return "redirect:/visitor/menu";
    }

    @GetMapping("visitor/show_order/{id}")
    public String showOrder(Model model, @PathVariable("id") UUID id){
        Order order = orderService.getOrder(id);
        model.addAttribute("order", order);
        List<Dish> remainingDishes = dishService.getRemainingDishes(id);
        model.addAttribute("remainingDishes", remainingDishes);
        model.addAttribute("orderForm", new OrderForm());
        model.addAttribute("ready", Status.READY);
        return "visitor/show_order";
    }

    @PostMapping("/visitor/show_order/add_dishes/{id}")
    public String addDishes(@PathVariable("id") UUID id, @ModelAttribute("orderForm") OrderForm orderForm) throws Exception {
        orderService.addDishes(id, orderForm);

        return "redirect:/visitor/show_order/{id}";
    }

    @PostMapping("/visitor/show_order/update_order/{id}/{dish_id}")
    public String deleteDish(@PathVariable("id") UUID id, @PathVariable("dish_id") UUID dish_id) {
        orderService.deleteDish(id, dish_id);
        Order order = orderService.getOrder(id);
        if (order.getDishes().isEmpty()) {
            orderService.deleteOrder(id);
            return "redirect:/visitor/menu";
        }
        return "redirect:/visitor/show_order/{id}";
    }

    @PostMapping("/visitor/pay/{id}")
    public String payOrder(@PathVariable("id") UUID id) {
        orderService.pay(id);
        orderService.deleteOrder(id);
        return "redirect:/visitor/menu";
    }

    @PostMapping("/visitor/show_order/feedback/{id}/{dish_id}")
    public String feedback(@PathVariable("dish_id") UUID dish_id, @RequestParam("number") int number, @RequestParam("text") String text) {
        dishService.addFeedback(dish_id, number, text);
        return "redirect:/visitor/show_order/{id}";
    }


//    @GetMapping("visitor/show_order")
//    public String showOrder(Model model) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String username = authentication.getName();
//        List<Order> orders = orderService.getAllOrdersByUsername(username);
//        model.addAttribute("orders", orders);
//        return "visitor/show_order";
//    }




}
