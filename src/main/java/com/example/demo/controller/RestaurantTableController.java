package com.example.demo.controller;

import com.example.demo.entity.RestaurantTable;
import com.example.demo.service.RestaurantTableService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tables")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600)
public class RestaurantTableController {

    private final RestaurantTableService restaurantTableService;

    @GetMapping("/restaurant/{restaurantId}")
    public ResponseEntity<List<RestaurantTable>> getRestaurantTables(@PathVariable Long restaurantId) {
        List<RestaurantTable> tables = restaurantTableService.getTablesByRestaurantId(restaurantId);
        return ResponseEntity.ok(tables);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RestaurantTable> getTableById(@PathVariable Long id) {
        RestaurantTable table = restaurantTableService.getTableById(id);
        return ResponseEntity.ok(table);
    }

    @PostMapping("/restaurant/{restaurantId}")
    public ResponseEntity<RestaurantTable> createTable(
            @PathVariable Long restaurantId,
            @RequestParam Integer tableNumber,
            @RequestParam Integer capacity) {
        RestaurantTable createdTable = restaurantTableService.createTable(restaurantId, tableNumber, capacity);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTable);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RestaurantTable> updateTable(
            @PathVariable Long id,
            @RequestParam(required = false) Integer tableNumber,
            @RequestParam(required = false) Integer capacity,
            @RequestParam(required = false) Boolean isAvailable) {
        RestaurantTable updatedTable = restaurantTableService.updateTable(id, tableNumber, capacity, isAvailable);
        return ResponseEntity.ok(updatedTable);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTable(@PathVariable Long id) {
        restaurantTableService.deleteTable(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/restaurant/{restaurantId}/available")
    public ResponseEntity<List<RestaurantTable>> getAvailableTables(@PathVariable Long restaurantId) {
        List<RestaurantTable> tables = restaurantTableService.getAvailableTablesByRestaurantId(restaurantId);
        return ResponseEntity.ok(tables);
    }

    @PutMapping("/{id}/availability")
    public ResponseEntity<Void> setAvailability(@PathVariable Long id, @RequestParam Boolean isAvailable) {
        restaurantTableService.setTableAvailability(id, isAvailable);
        return ResponseEntity.ok().build();
    }
}
