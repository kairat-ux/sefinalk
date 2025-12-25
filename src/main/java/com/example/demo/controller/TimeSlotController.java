package com.example.demo.controller;

import com.example.demo.dto.request.TimeSlotCreateRequestDTO;
import com.example.demo.dto.response.TimeSlotResponseDTO;
import com.example.demo.service.TimeSlotService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/timeslots")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600)
public class TimeSlotController {

    private final TimeSlotService timeSlotService;

    @GetMapping("/restaurant/{restaurantId}")
    public ResponseEntity<List<TimeSlotResponseDTO>> getRestaurantTimeSlots(
            @PathVariable Long restaurantId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<TimeSlotResponseDTO> timeSlots = timeSlotService.getRestaurantTimeSlots(restaurantId, date);
        return ResponseEntity.ok(timeSlots);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TimeSlotResponseDTO> getTimeSlotById(@PathVariable Long id) {
        TimeSlotResponseDTO timeSlot = timeSlotService.getTimeSlotById(id);
        return ResponseEntity.ok(timeSlot);
    }

    @PostMapping
    public ResponseEntity<TimeSlotResponseDTO> createTimeSlot(@Valid @RequestBody TimeSlotCreateRequestDTO request) {
        TimeSlotResponseDTO timeSlot = timeSlotService.createTimeSlot(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(timeSlot);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTimeSlot(@PathVariable Long id) {
        timeSlotService.deleteTimeSlot(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/restaurant/{restaurantId}/available")
    public ResponseEntity<List<TimeSlotResponseDTO>> getAvailableTimeSlots(
            @PathVariable Long restaurantId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<TimeSlotResponseDTO> timeSlots = timeSlotService.getAvailableTimeSlots(restaurantId, date);
        return ResponseEntity.ok(timeSlots);
    }

    @PutMapping("/{id}/block")
    public ResponseEntity<Void> blockTimeSlot(@PathVariable Long id, @RequestParam(required = false) String reason) {
        timeSlotService.blockTimeSlot(id, reason);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/unblock")
    public ResponseEntity<Void> unblockTimeSlot(@PathVariable Long id) {
        timeSlotService.unblockTimeSlot(id);
        return ResponseEntity.ok().build();
    }
}
