package com.example.demo.controller;

import com.example.demo.dto.request.RestaurantCreateRequestDTO;
import com.example.demo.dto.response.RestaurantDetailResponseDTO;
import com.example.demo.security.UserPrincipal;
import com.example.demo.service.RestaurantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/restaurants")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
public class RestaurantController {

    private final RestaurantService restaurantService;

    private Long getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof UserPrincipal) {
            return ((UserPrincipal) auth.getPrincipal()).getId();
        }
        return null;
    }

    @PostMapping
    public ResponseEntity<RestaurantDetailResponseDTO> createRestaurant(
            @Valid @RequestBody RestaurantCreateRequestDTO request) {
        Long ownerId = getCurrentUserId();
        if (ownerId == null) {
            throw new RuntimeException("User not authenticated");
        }
        RestaurantDetailResponseDTO restaurant = restaurantService.createRestaurant(request, ownerId);
        return ResponseEntity.status(HttpStatus.CREATED).body(restaurant);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RestaurantDetailResponseDTO> getRestaurantById(@PathVariable Long id) {
        RestaurantDetailResponseDTO restaurant = restaurantService.getRestaurantById(id);
        return ResponseEntity.ok(restaurant);
    }

    @GetMapping
    public ResponseEntity<List<RestaurantDetailResponseDTO>> getAllRestaurants() {
        List<RestaurantDetailResponseDTO> restaurants = restaurantService.getAllRestaurants();
        return ResponseEntity.ok(restaurants);
    }

    @GetMapping("/city/{city}")
    public ResponseEntity<List<RestaurantDetailResponseDTO>> getRestaurantsByCity(@PathVariable String city) {
        List<RestaurantDetailResponseDTO> restaurants = restaurantService.getRestaurantsByCity(city);
        return ResponseEntity.ok(restaurants);
    }

    @GetMapping("/owner/{ownerId}")
    public ResponseEntity<List<RestaurantDetailResponseDTO>> getOwnerRestaurants(@PathVariable Long ownerId) {
        List<RestaurantDetailResponseDTO> restaurants = restaurantService.getOwnerRestaurants(ownerId);
        return ResponseEntity.ok(restaurants);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateRestaurant(@PathVariable Long id,
                                                 @Valid @RequestBody RestaurantCreateRequestDTO request) {
        restaurantService.updateRestaurant(id, request);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRestaurant(@PathVariable Long id) {
        restaurantService.deleteRestaurant(id);
        return ResponseEntity.noContent().build();
    }
}