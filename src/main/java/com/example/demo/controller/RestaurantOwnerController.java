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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/owner")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
@PreAuthorize("hasAnyRole('OWNER', 'ADMIN')")
public class RestaurantOwnerController {

    private final RestaurantService restaurantService;

    @GetMapping("/restaurants")
    public ResponseEntity<List<RestaurantDetailResponseDTO>> getMyRestaurants(
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        return ResponseEntity.ok(restaurantService.getOwnerRestaurants(userPrincipal.getId()));
    }

    @PostMapping("/restaurants")
    public ResponseEntity<RestaurantDetailResponseDTO> createRestaurant(
            @Valid @RequestBody RestaurantCreateRequestDTO request,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        RestaurantDetailResponseDTO restaurant = restaurantService.createRestaurant(request, userPrincipal.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(restaurant);
    }

    @PutMapping("/restaurants/{id}")
    public ResponseEntity<Void> updateRestaurant(
            @PathVariable Long id,
            @Valid @RequestBody RestaurantUpdateRequestDTO request,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        
        List<RestaurantDetailResponseDTO> ownerRestaurants = restaurantService.getOwnerRestaurants(userPrincipal.getId());
        boolean isOwner = ownerRestaurants.stream().anyMatch(r -> r.getId().equals(id));
        
        if (!isOwner) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        RestaurantCreateRequestDTO updateDTO = new RestaurantCreateRequestDTO();
        updateDTO.setName(request.getName());
        updateDTO.setDescription(request.getDescription());
        updateDTO.setAddress(request.getAddress());
        updateDTO.setCity(request.getCity());
        updateDTO.setZipCode(request.getZipCode());
        updateDTO.setPhone(request.getPhone());
        updateDTO.setEmail(request.getEmail());

        restaurantService.updateRestaurant(id, updateDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/restaurants/{id}")
    public ResponseEntity<Void> deleteRestaurant(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        
        List<RestaurantDetailResponseDTO> ownerRestaurants = restaurantService.getOwnerRestaurants(userPrincipal.getId());
        boolean isOwner = ownerRestaurants.stream().anyMatch(r -> r.getId().equals(id));
        
        if (!isOwner) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        restaurantService.deleteRestaurant(id);
        return ResponseEntity.ok().build();
    }
}
