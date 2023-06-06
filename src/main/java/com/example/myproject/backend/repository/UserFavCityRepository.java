package com.example.myproject.backend.repository;

import com.example.myproject.backend.domain.entity.UserFavCityEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserFavCityRepository extends JpaRepository<UserFavCityEntity, Long> {

    @Query("select max(entityInfo.userFavCityId) from UserFavCityEntity entityInfo")
    Long findMaxUserFavCityId();

    @Query("select entityInfo from UserFavCityEntity entityInfo where entityInfo.userId = ?1")
    List<UserFavCityEntity> findFavListByuserId(Long userId);


}
