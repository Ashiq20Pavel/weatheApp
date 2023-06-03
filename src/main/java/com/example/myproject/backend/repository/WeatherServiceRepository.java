package com.example.myproject.backend.repository;

import com.example.myproject.backend.domain.entity.WeatherStatusEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface WeatherServiceRepository extends JpaRepository<WeatherStatusEntity, Integer> {

    @Query("select entityInfo.description from WeatherStatusEntity entityInfo where entityInfo.code = ?1")
    String findDesByCode(Integer code);

}
