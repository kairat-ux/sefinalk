package com.example.demo.service;

import com.example.demo.entity.RestaurantTable;

import java.util.List;

public interface RestaurantTableService {
    List<RestaurantTable> getTablesByRestaurantId(Long restaurantId);
    List<RestaurantTable> getAvailableTablesByRestaurantId(Long restaurantId);
    RestaurantTable getTableById(Long id);
    RestaurantTable createTable(Long restaurantId, Integer tableNumber, Integer capacity);
    RestaurantTable updateTable(Long id, Integer tableNumber, Integer capacity, Boolean isAvailable);
    void deleteTable(Long id);
    void setTableAvailability(Long id, Boolean isAvailable);
}
