package com.example.myproject.backend.util;

import com.example.myproject.backend.domain.dto.WeatherDTO;
import com.example.myproject.backend.domain.entity.CityInfoEntity;
import com.example.myproject.backend.repository.CityInfoRepository;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;

@Service
public class WeatherInfoService {

    @Autowired
    CityInfoRepository cityInfoRepository;

    private static final String API_URL = "https://api.open-meteo.com/v1/forecast";

    public WeatherDTO getWeatherByCity(Long cityId) {

        CityInfoEntity cityInfoEntity = cityInfoRepository.getInfoByCityId(cityId);

        float latitude = Float.parseFloat(cityInfoEntity.getLat());
        float longitude = Float.parseFloat(cityInfoEntity.getLng());


        RestTemplate restTemplate = new RestTemplate();

        String url = API_URL + "?latitude=" + latitude + "&longitude=" + longitude + "&hourly=temperature_2m,rain,windspeed_10m&current_weather=true";
        try {
            String response = restTemplate.getForObject(url, String.class);

            JSONObject jsonObject = new JSONObject(response);
            JSONObject currentWeather = jsonObject.getJSONObject("current_weather");

            double temperature2m = currentWeather.getDouble("temperature");
            double windSpeed10m = currentWeather.getDouble("windspeed");
            Integer weatherCode = currentWeather.getInt("weathercode");

            WeatherDTO weatherDTO = new WeatherDTO();
            weatherDTO.setTemperature(temperature2m);
            weatherDTO.setWindSpeed(windSpeed10m);
            weatherDTO.setWeatherStatus(weatherCode);

            return weatherDTO;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }


    public ArrayList<WeatherDTO> getWeatherHourlyByCity(Long cityId) {

        CityInfoEntity cityInfoEntity = cityInfoRepository.getInfoByCityId(cityId);

        float latitude = Float.parseFloat(cityInfoEntity.getLat());
        float longitude = Float.parseFloat(cityInfoEntity.getLng());


        RestTemplate restTemplate = new RestTemplate();
        String url = API_URL + "?latitude=" + latitude + "&longitude=" + longitude + "&hourly=temperature_2m,rain,windspeed_10m&current_weather=true";

        try {
            String response = restTemplate.getForObject(url, String.class);

            JSONObject jsonObject = new JSONObject(response);
            JSONObject hourlyData = jsonObject.getJSONObject("hourly");

            JSONArray timeArray = hourlyData.getJSONArray("time");
            JSONArray temperature_2m = hourlyData.getJSONArray("temperature_2m");
            JSONArray rain = hourlyData.getJSONArray("rain");
            JSONArray windspeed_10m = hourlyData.getJSONArray("windspeed_10m");

            ArrayList<WeatherDTO> arrayList = new ArrayList();
            for (int i = 0; i < timeArray.length(); i++) {
                WeatherDTO weatherDTO = new WeatherDTO();
                String time = timeArray.getString(i);
                double temperature = temperature_2m.getDouble(i);
                double rainData = rain.getDouble(i);
                double windspeed = windspeed_10m.getDouble(i);

                weatherDTO.setTime(time);
                weatherDTO.setTemperature(temperature);
                weatherDTO.setHumidity(rainData);
                weatherDTO.setWindSpeed(windspeed);
                arrayList.add(weatherDTO);
            }

            return arrayList;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
