package com.example.SunriseSunset.controller;

import com.example.SunriseSunset.model.LocationEntity;
import com.example.SunriseSunset.service.LocationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/locations")
public class LocationController {
    private final LocationService locationService;

    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    @PostMapping
    public ResponseEntity<LocationEntity> create(@RequestBody LocationEntity location) {
        return ResponseEntity.ok(locationService.create(location));
    }

    @GetMapping
    public ResponseEntity<List<LocationEntity>> findAll() {
        return ResponseEntity.ok(locationService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LocationEntity> findById(@PathVariable Integer id) {
        return locationService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{locationId}/sunrise_and_sunset/{sunriseSunsetId}")
    public ResponseEntity<Void> addSunriseSunset(@PathVariable Integer locationId, @PathVariable Integer sunriseSunsetId) {
        locationService.addSunriseSunsetToLocation(locationId, sunriseSunsetId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{locationId}/sunrise-sunset/{sunriseSunsetId}")
    public ResponseEntity<Void> removeSunriseSunset(@PathVariable Integer locationId, @PathVariable Integer sunriseSunsetId) {
        locationService.removeSunriseSunsetFromLocation(locationId, sunriseSunsetId);
        return ResponseEntity.ok().build();
    }
}