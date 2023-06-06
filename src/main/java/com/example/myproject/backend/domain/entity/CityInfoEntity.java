package com.example.myproject.backend.domain.entity;

import org.json.JSONObject;

import javax.persistence.*;

@Entity
@Table(name = "city_info")
public class CityInfoEntity {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "country")
    private String country;

    @Column(name = "city")
    private String city;

    @Column(name = "lat")
    private String lat;

    @Column(name = "lng")
    private String lng;

    @Transient
    private JSONObject weatherDTO;

    @Transient
    private boolean isFavorite;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public JSONObject getWeatherDTO() {
        return weatherDTO;
    }

    public String setWeatherDTO(JSONObject weatherDTO) {
        this.weatherDTO = weatherDTO;
        return null;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }
}
