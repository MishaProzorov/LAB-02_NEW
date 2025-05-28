package com.example.SunriseSunset.controller;

import com.example.SunriseSunset.model.SunriseSunsetEntity;
import com.example.SunriseSunset.service.SunriseSunsetService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/sunrise_and_sunset")
public class SunriseSunsetController {
    private final SunriseSunsetService sunriseAndSunsetService;

    public SunriseSunsetController(SunriseSunsetService sunriseAndSunsetService) {
        this.sunriseAndSunsetService = sunriseAndSunsetService;
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody SunriseSunsetEntity request) {
        try {
            SunriseSunsetEntity result = sunriseAndSunsetService.createOrGet(request);
            return ResponseEntity.ok(result);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping
    public ResponseEntity<List<SunriseSunsetEntity>> findAll() {
        return ResponseEntity.ok(sunriseAndSunsetService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SunriseSunsetEntity> findById(@PathVariable Integer id) {
        return sunriseAndSunsetService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/by-date-and-coordinates")
    public ResponseEntity<?> getByDateAndCoordinates(
            @RequestParam("date") String date,
            @RequestParam("latitude") String latitude,
            @RequestParam("longitude") String longitude) {
        try {
            LocalDate localDate = LocalDate.parse(date);
            BigDecimal lat = new BigDecimal(latitude);
            BigDecimal lon = new BigDecimal(longitude);

            return sunriseAndSunsetService.findByDateAndCoordinates(localDate, lat, lon)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Invalid input parameters: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable Integer id,
            @RequestBody SunriseSunsetEntity sunriseAndSunset) {
        try {
            return ResponseEntity.ok(sunriseAndSunsetService.update(id, sunriseAndSunset));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        sunriseAndSunsetService.delete(id);
        return ResponseEntity.noContent().build();
    }
}