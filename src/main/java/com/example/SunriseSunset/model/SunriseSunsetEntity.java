package com.example.SunriseSunset.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "sunrise_and_sunset")
public class SunriseSunsetEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "latitude", nullable = false, precision = 9, scale = 6)
    private BigDecimal latitude;

    @Column(name = "longitude", nullable = false, precision = 9, scale = 6)
    private BigDecimal longitude;

    @Column(name = "sunrise", nullable = false)
    private OffsetDateTime sunrise;

    @Column(name = "sunset", nullable = false)
    private OffsetDateTime sunset;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "sunrise_sunset_locations",
            joinColumns = @JoinColumn(name = "sunrise_sunset_id"),
            inverseJoinColumns = @JoinColumn(name = "location_id")
    )
    @JsonManagedReference
    private Set<LocationEntity> locations = new HashSet<>();
    public SunriseSunsetEntity() {}
    public SunriseSunsetEntity(LocalDate date, BigDecimal latitude, BigDecimal longitude, OffsetDateTime sunrise, OffsetDateTime sunset) {
        this.date = date;
        this.latitude = latitude;
        this.longitude = longitude;
        this.sunrise = sunrise;
        this.sunset = sunset;
    }
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    public BigDecimal getLatitude() { return latitude; }
    public void setLatitude(BigDecimal latitude) { this.latitude = latitude; }
    public BigDecimal getLongitude() { return longitude; }
    public void setLongitude(BigDecimal longitude) { this.longitude = longitude; }
    public OffsetDateTime getSunrise() { return sunrise; }
    public void setSunrise(OffsetDateTime sunrise) { this.sunrise = sunrise; }
    public OffsetDateTime getSunset() { return sunset; }
    public void setSunset(OffsetDateTime sunset) { this.sunset = sunset; }
    public Set<LocationEntity> getLocations() { return locations; }
    public void setLocations(Set<LocationEntity> locations) { this.locations = locations; }
    public void addLocation(LocationEntity location) {
        locations.add(location);
        location.getSunriseSunsets().add(this);
    }
    public void removeLocation(LocationEntity location) {
        locations.remove(location);
        location.getSunriseSunsets().remove(this);
    }
}