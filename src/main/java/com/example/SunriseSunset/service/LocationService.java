package com.example.SunriseSunset.service;
import com.example.SunriseSunset.model.LocationEntity;
import com.example.SunriseSunset.model.SunriseSunsetEntity;
import com.example.SunriseSunset.Repository.LocationRepository;
import com.example.SunriseSunset.Repository.SunriseSunsetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class LocationService {
    private final LocationRepository locationRepository;
    private final SunriseSunsetRepository sunriseSunsetRepository;

    @Autowired
    public LocationService(LocationRepository locationRepository, SunriseSunsetRepository sunriseSunsetRepository) {
        this.locationRepository = locationRepository;
        this.sunriseSunsetRepository = sunriseSunsetRepository;
    }
    public LocationEntity create(LocationEntity location) {
        return locationRepository.save(location);
    }
    public Optional<LocationEntity> findById(Integer id) {
        return locationRepository.findById(id);
    }
    public List<LocationEntity> findAll() {
        return locationRepository.findAll();
    }
    public void addSunriseSunsetToLocation(Integer locationId, Integer sunriseSunsetId) {
        LocationEntity location = locationRepository.findById(locationId)
                .orElseThrow(() -> new RuntimeException("Location not found with id: " + locationId));
        SunriseSunsetEntity sunriseSunset = sunriseSunsetRepository.findById(sunriseSunsetId)
                .orElseThrow(() -> new RuntimeException("SunriseSunset not found with id: " + sunriseSunsetId));

        location.getSunriseSunsets().add(sunriseSunset);
        sunriseSunset.getLocations().add(location);

        locationRepository.save(location);
    }
    public void removeSunriseSunsetFromLocation(Integer locationId, Integer sunriseSunsetId) {
        LocationEntity location = locationRepository.findById(locationId)
                .orElseThrow(() -> new RuntimeException("Location not found with id: " + locationId));
        SunriseSunsetEntity sunriseSunset = sunriseSunsetRepository.findById(sunriseSunsetId)
                .orElseThrow(() -> new RuntimeException("SunriseSunset not found with id: " + sunriseSunsetId));

        location.getSunriseSunsets().remove(sunriseSunset);
        sunriseSunset.getLocations().remove(location);

        locationRepository.save(location);
    }
}