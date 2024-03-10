package com.example.RestaurantModel.data.forms;

import com.example.RestaurantModel.data.models.Dish;

import java.util.ArrayList;
import java.util.List;

public class OrderForm {
    public OrderForm() {}
    public List<String> getSelectedDishes() {
        return selectedDishes;
    }

    public void setSelectedDishes(List<String> selectedDishes) {
        this.selectedDishes = selectedDishes;
    }

    private List<String> selectedDishes = new ArrayList<>();
}
