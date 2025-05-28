package com.example.SunriseSunset.service;
import com.example.SunriseSunset.model.SunriseSunsetEntity;
import com.example.SunriseSunset.Repository.SunriseSunsetRepository;
import org.shredzone.commons.suncalc.SunTimes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
@Service
@Transactional
public class SunriseSunsetService {
    private final SunriseSunsetRepository repository;

    @Autowired
    public SunriseSunsetService(SunriseSunsetRepository repository) {
        this.repository = repository;
    }
    public SunriseSunsetEntity createOrGet(SunriseSunsetEntity request) {
        validateRequest(request);

        LocalDate date = request.getDate();
        BigDecimal latitude = request.getLatitude();
        BigDecimal longitude = request.getLongitude();

        Optional<SunriseSunsetEntity> existing = repository.findByDateAndCoordinates(date, latitude, longitude);
        if (existing.isPresent()) {
            return existing.get();
        }
        SunriseSunsetEntity entity = calculateSunriseSunset(date, latitude, longitude);
        return repository.save(entity);
    }

    private SunriseSunsetEntity calculateSunriseSunset(LocalDate date, BigDecimal latitude, BigDecimal longitude) {
        SunTimes times = SunTimes.compute()
                .on(date.getYear(), date.getMonthValue(), date.getDayOfMonth())
                .at(latitude.doubleValue(), longitude.doubleValue())
                .execute();
                ZoneId zoneId = ZoneId.of("Europe/Kiev"); // Можно заменить на нужную зону
        OffsetDateTime sunrise = times.getRise() != null ? OffsetDateTime.ofInstant(times.getRise().toInstant(), zoneId) : null;
        OffsetDateTime sunset = times.getSet() != null ? OffsetDateTime.ofInstant(times.getSet().toInstant(), zoneId) : null;
        if (sunrise == null || sunset == null) {
            throw new IllegalStateException("Could not calculate sunrise or sunset for the given date and coordinates");
        }

        SunriseSunsetEntity entity = new SunriseSunsetEntity();
        entity.setDate(date);
        entity.setLatitude(latitude);
        entity.setLongitude(longitude);
        entity.setSunrise(sunrise);
        entity.setSunset(sunset);

        return entity;
    }
    private void validateRequest(SunriseSunsetEntity request) {
        if (request == null) {
            throw new IllegalArgumentException("Request cannot be null");
        }
        if (request.getDate() == null) {
            throw new IllegalArgumentException("Date cannot be null");
        }
        if (request.getLatitude() == null) {
            throw new IllegalArgumentException("Latitude cannot be null");
        }
        if (request.getLongitude() == null) {
            throw new IllegalArgumentException("Longitude cannot be null");
        }
    }
    public List<SunriseSunsetEntity> findAll() {
        return repository.findAll();
    }

    public Optional<SunriseSunsetEntity> findById(Integer id) {
        return repository.findById(id);
    }
    public Optional<SunriseSunsetEntity> findByDateAndCoordinates(
            LocalDate date, BigDecimal latitude, BigDecimal longitude) {
        return repository.findByDateAndCoordinates(date, latitude, longitude);
    }
    public SunriseSunsetEntity update(Integer id, SunriseSunsetEntity updatedEntity) {
        validateRequest(updatedEntity);

        return repository.findById(id)
                .map(entity -> {
                    entity.setDate(updatedEntity.getDate());
                    entity.setLatitude(updatedEntity.getLatitude());
                    entity.setLongitude(updatedEntity.getLongitude());

                    SunriseSunsetEntity recalculated = calculateSunriseSunset(
                            updatedEntity.getDate(),
                            updatedEntity.getLatitude(),
                            updatedEntity.getLongitude());

                    entity.setSunrise(recalculated.getSunrise());
                    entity.setSunset(recalculated.getSunset());
                    return repository.save(entity);
                })
                .orElseThrow(() -> new RuntimeException("Entity not found with id: " + id));
    }
    public void delete(Integer id) {
        repository.deleteById(id);
    }
}