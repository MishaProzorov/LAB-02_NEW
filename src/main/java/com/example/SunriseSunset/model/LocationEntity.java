package com.example.SunriseSunset.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "locations")
public class LocationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "country")
    private String country;


    @ManyToMany(mappedBy = "locations")
    @JsonBackReference
    private Set<SunriseSunsetEntity> sunriseSunsets = new HashSet<>();

    public LocationEntity() {}

    public LocationEntity(String name, String country) {
        this.name = name;
        this.country = country;

    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }

    public Set<SunriseSunsetEntity> getSunriseSunsets() { return sunriseSunsets; }
    public void setSunriseSunsets(Set<SunriseSunsetEntity> sunriseSunsets) { this.sunriseSunsets = sunriseSunsets; }
}