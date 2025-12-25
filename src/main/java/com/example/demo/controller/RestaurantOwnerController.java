package com.example.demo.controller;

import com.example.demo.dto.request.RestaurantCreateRequestDTO;
import com.example.demo.dto.request.RestaurantUpdateRequestDTO;
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
@RequestMapping("/api/owner/restaurants")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
public class RestaurantOwnerController {

    private final RestaurantService restaurantService;

    private UserPrincipal getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof UserPrincipal) {
            return (UserPrincipal) auth.getPrincipal();
        }
        throw new RuntimeException("User not authenticated");
    }

    @GetMapping
    public ResponseEntity<List<RestaurantDetailResponseDTO>> getOwnerRestaurants() {
        UserPrincipal userPrincipal = getCurrentUser();
        return ResponseEntity.ok(restaurantService.getOwnerRestaurants(userPrincipal.getId()));
    }

    @PostMapping
    public ResponseEntity<RestaurantDetailResponseDTO> createRestaurant(
            @Valid @RequestBody RestaurantCreateRequestDTO request) {
        UserPrincipal userPrincipal = getCurrentUser();
        RestaurantDetailResponseDTO restaurant = restaurantService.createRestaurant(request, userPrincipal.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(restaurant);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RestaurantDetailResponseDTO> getRestaurant(@PathVariable Long id) {
        UserPrincipal userPrincipal = getCurrentUser();
        List<RestaurantDetailResponseDTO> ownerRestaurants = restaurantService.getOwnerRestaurants(userPrincipal.getId());
        boolean isOwner = ownerRestaurants.stream().anyMatch(r -> r.getId().equals(id));

        if (!isOwner) {
            throw new RuntimeException("Unauthorized");
        }

        return ResponseEntity.ok(restaurantService.getRestaurantById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateRestaurant(@PathVariable Long id,
                                                 @Valid @RequestBody RestaurantUpdateRequestDTO request) {
        UserPrincipal userPrincipal = getCurrentUser();
        List<RestaurantDetailResponseDTO> ownerRestaurants = restaurantService.getOwnerRestaurants(userPrincipal.getId());
        boolean isOwner = ownerRestaurants.stream().anyMatch(r -> r.getId().equals(id));

        if (!isOwner) {
            throw new RuntimeException("Unauthorized");
        }

        RestaurantCreateRequestDTO updateDTO = RestaurantCreateRequestDTO.builder()
                .name(request.getName())
                .description(request.getDescription())
                .address(request.getAddress())
                .city(request.getCity())
                .zipCode(request.getZipCode())
                .phone(request.getPhone())
                .email(request.getEmail())
                .build();

        restaurantService.updateRestaurant(id, updateDTO);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRestaurant(@PathVariable Long id) {
        UserPrincipal userPrincipal = getCurrentUser();
        List<RestaurantDetailResponseDTO> ownerRestaurants = restaurantService.getOwnerRestaurants(userPrincipal.getId());
        boolean isOwner = ownerRestaurants.stream().anyMatch(r -> r.getId().equals(id));

        if (!isOwner) {
            throw new RuntimeException("Unauthorized");
        }

        restaurantService.deleteRestaurant(id);
        return ResponseEntity.noContent().build();
    }
}