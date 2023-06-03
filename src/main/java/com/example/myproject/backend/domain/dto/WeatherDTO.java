package com.example.myproject.backend.domain.dto;

public class WeatherDTO {

    private double temperature;
    private double humidity;
    private double windSpeed;
    private double rain;
    private String time;
    private Integer weatherStatus;

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public double getHumidity() {
        return humidity;
    }

    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(double windSpeed) {
        this.windSpeed = windSpeed;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public double getRain() {
        return rain;
    }

    public void setRain(double rain) {
        this.rain = rain;
    }

    public Integer getWeatherStatus() {
        return weatherStatus;
    }

    public void setWeatherStatus(Integer weatherStatus) {
        this.weatherStatus = weatherStatus;
    }
}
