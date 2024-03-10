package com.example.RestaurantModel.data.models;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "dish")
public class Dish {

    public Dish() {
    }
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "name")
    private String name;

    @Column(name = "cooking time")
    private int cookingTime;

    @Column(name = "price")
    private int price;

    @Column(name = "count")
    private int count;

    @Column(name = "sold")
    private Integer sold;

    @Column(name = "avg_estimation")
    private Double avgEstimation;

    @Column(name = "feedback")
    private List<String> feedback = new ArrayList<>();

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCookingTime() {
        return cookingTime;
    }

    public void setCookingTime(int cookingTime) {
        this.cookingTime = cookingTime;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getCount() { return count; }

    public void setCount(int count) { this.count = count; }

    public Integer getSold() {
        return sold;
    }

    public void setSold(Integer sold) {
        this.sold = sold;
    }

    public Double getAvgEstimation() {
        return avgEstimation;
    }

    public void setAvgEstimation(Double avgEstimation) {
        this.avgEstimation = avgEstimation;
    }

    public List<String> getFeedback() {
        return feedback;
    }

    public void setFeedback(List<String> feedback) {
        this.feedback = feedback;
    }
}
