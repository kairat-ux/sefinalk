package com.example.demo.service.impl;

import com.example.demo.entity.Cuisine;
import com.example.demo.repository.CuisineRepository;
import com.example.demo.service.CuisineService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CuisineServiceImpl implements CuisineService {

    private final CuisineRepository cuisineRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Cuisine> getAllCuisines() {
        return cuisineRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Cuisine getCuisineById(Long id) {
        return cuisineRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cuisine not found with id: " + id));
    }

    @Override
    public Cuisine createCuisine(String name, String description) {
        if (cuisineRepository.findByName(name).isPresent()) {
            throw new RuntimeException("Cuisine already exists with name: " + name);
        }

        Cuisine cuisine = Cuisine.builder()
                .name(name)
                .description(description)
                .createdAt(LocalDateTime.now())
                .build();

        return cuisineRepository.save(cuisine);
    }

    @Override
    public Cuisine updateCuisine(Long id, String name, String description) {
        Cuisine cuisine = getCuisineById(id);

        if (name != null && !name.equals(cuisine.getName())) {
            if (cuisineRepository.findByName(name).isPresent()) {
                throw new RuntimeException("Cuisine already exists with name: " + name);
            }
            cuisine.setName(name);
        }

        if (description != null) {
            cuisine.setDescription(description);
        }

        return cuisineRepository.save(cuisine);
    }

    @Override
    public void deleteCuisine(Long id) {
        Cuisine cuisine = getCuisineById(id);
        cuisineRepository.delete(cuisine);
    }

    @Override
    @Transactional(readOnly = true)
    public Cuisine getCuisineByName(String name) {
        return cuisineRepository.findByName(name)
                .orElseThrow(() -> new RuntimeException("Cuisine not found with name: " + name));
    }
}
