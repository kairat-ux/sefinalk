package com.example.demo.service.impl;

import com.example.demo.entity.Restaurant;
import com.example.demo.entity.RestaurantTable;
import com.example.demo.repository.RestaurantRepository;
import com.example.demo.repository.RestaurantTableRepository;
import com.example.demo.service.RestaurantTableService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class RestaurantTableServiceImpl implements RestaurantTableService {

    private final RestaurantTableRepository restaurantTableRepository;
    private final RestaurantRepository restaurantRepository;

    @Override
    @Transactional(readOnly = true)
    public List<RestaurantTable> getTablesByRestaurantId(Long restaurantId) {
        return restaurantTableRepository.findByRestaurantId(restaurantId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RestaurantTable> getAvailableTablesByRestaurantId(Long restaurantId) {
        return restaurantTableRepository.findByRestaurantIdAndIsAvailableTrue(restaurantId);
    }

    @Override
    @Transactional(readOnly = true)
    public RestaurantTable getTableById(Long id) {
        return restaurantTableRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Table not found with id: " + id));
    }

    @Override
    public RestaurantTable createTable(Long restaurantId, Integer tableNumber, Integer capacity) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RuntimeException("Restaurant not found with id: " + restaurantId));

        if (restaurantTableRepository.findByRestaurantIdAndTableNumber(restaurantId, tableNumber).isPresent()) {
            throw new RuntimeException("Table number already exists: " + tableNumber);
        }

        RestaurantTable table = RestaurantTable.builder()
                .restaurant(restaurant)
                .tableNumber(tableNumber)
                .capacity(capacity)
                .location(RestaurantTable.TableLocation.CENTER)
                .isAvailable(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        return restaurantTableRepository.save(table);
    }

    @Override
    public RestaurantTable updateTable(Long id, Integer tableNumber, Integer capacity, Boolean isAvailable) {
        RestaurantTable table = getTableById(id);

        if (tableNumber != null && !tableNumber.equals(table.getTableNumber())) {
            if (restaurantTableRepository.findByRestaurantIdAndTableNumber(
                    table.getRestaurant().getId(), tableNumber).isPresent()) {
                throw new RuntimeException("Table number already exists: " + tableNumber);
            }
            table.setTableNumber(tableNumber);
        }

        if (capacity != null) {
            table.setCapacity(capacity);
        }

        if (isAvailable != null) {
            table.setIsAvailable(isAvailable);
        }

        table.setUpdatedAt(LocalDateTime.now());

        return restaurantTableRepository.save(table);
    }

    @Override
    public void deleteTable(Long id) {
        RestaurantTable table = getTableById(id);
        restaurantTableRepository.delete(table);
    }

    @Override
    public void setTableAvailability(Long id, Boolean isAvailable) {
        RestaurantTable table = getTableById(id);
        table.setIsAvailable(isAvailable);
        table.setUpdatedAt(LocalDateTime.now());
        restaurantTableRepository.save(table);
    }
}
