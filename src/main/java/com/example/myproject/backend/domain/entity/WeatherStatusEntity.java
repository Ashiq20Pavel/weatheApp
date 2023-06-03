package com.example.myproject.backend.domain.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "weather_status")
public class WeatherStatusEntity {

    @Id
    @Column(name = "code")
    private Integer code;

    @Column(name = "description")
    private String description;


    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
