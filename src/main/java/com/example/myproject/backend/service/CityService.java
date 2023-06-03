package com.example.myproject.backend.service;

import com.example.myproject.backend.domain.entity.CityInfoEntity;
import com.example.myproject.backend.repository.CityInfoRepository;
import com.example.myproject.backend.util.WeatherInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CityService {

    @Autowired
    CityInfoRepository cityInfoRepository;

    @Autowired
    WeatherInfoService weatherInfoService;

    

    public List<CityInfoEntity> getCityByAlphabet(char alphabet) {
        return cityInfoRepository.findAll().stream()
                .filter(city -> city.getCity().toUpperCase().startsWith(String.valueOf(alphabet)))
                .collect(Collectors.toList());
    }
}
