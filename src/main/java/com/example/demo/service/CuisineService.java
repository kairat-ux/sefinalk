package com.example.demo.service;

import com.example.demo.entity.Cuisine;

import java.util.List;

public interface CuisineService {
    List<Cuisine> getAllCuisines();
    Cuisine getCuisineById(Long id);
    Cuisine createCuisine(String name, String description);
    Cuisine updateCuisine(Long id, String name, String description);
    void deleteCuisine(Long id);
    Cuisine getCuisineByName(String name);
}
