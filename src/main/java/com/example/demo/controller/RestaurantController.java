// ПУТЬ: src/main/java/com/example/demo/controller/RestaurantController.java

package com.example.demo.controller;

import com.example.demo.dto.request.RestaurantCreateRequestDTO;
import com.example.demo.dto.response.RestaurantDetailResponseDTO;
import com.example.demo.service.RestaurantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/restaurants")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600)
public class RestaurantController {

    private final RestaurantService restaurantService;

    @PostMapping
    public ResponseEntity<RestaurantDetailResponseDTO> createRestaurant(
            @Valid @RequestBody RestaurantCreateRequestDTO request) {
        // ownerId будет получен из Spring Security context
        RestaurantDetailResponseDTO restaurant = restaurantService.createRestaurant(request, 1L);
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