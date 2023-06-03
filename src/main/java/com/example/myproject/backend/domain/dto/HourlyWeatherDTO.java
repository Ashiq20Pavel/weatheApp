package com.example.myproject.backend.domain.dto;

import org.json.JSONArray;

import java.lang.reflect.Array;

public class HourlyWeatherDTO {

    private JSONArray time;
    private JSONArray temperature_2m;
    private JSONArray relativehumidity_2m;
    private JSONArray windspeed_10m;


    public JSONArray getTime() {
        return time;
    }

    public void setTime(JSONArray time) {
        this.time = time;
    }

    public JSONArray getTemperature_2m() {
        return temperature_2m;
    }

    public void setTemperature_2m(JSONArray temperature_2m) {
        this.temperature_2m = temperature_2m;
    }

    public JSONArray getRelativehumidity_2m() {
        return relativehumidity_2m;
    }

    public void setRelativehumidity_2m(JSONArray relativehumidity_2m) {
        this.relativehumidity_2m = relativehumidity_2m;
    }

    public JSONArray getWindspeed_10m() {
        return windspeed_10m;
    }

    public void setWindspeed_10m(JSONArray windspeed_10m) {
        this.windspeed_10m = windspeed_10m;
    }
}
