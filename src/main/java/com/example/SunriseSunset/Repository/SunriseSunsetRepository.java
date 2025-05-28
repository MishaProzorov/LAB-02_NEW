package com.example.SunriseSunset.Repository;

import com.example.SunriseSunset.model.SunriseSunsetEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

public interface SunriseSunsetRepository extends JpaRepository<SunriseSunsetEntity, Integer> {
    @Query("SELECT s FROM SunriseSunsetEntity s WHERE s.date = :date AND s.latitude = :latitude AND s.longitude = :longitude")
    Optional<SunriseSunsetEntity> findByDateAndCoordinates(
            @Param("date") LocalDate date,
            @Param("latitude") BigDecimal latitude,
            @Param("longitude") BigDecimal longitude);
}