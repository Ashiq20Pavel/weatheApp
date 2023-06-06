package com.example.myproject.backend.repository;

import com.example.myproject.backend.domain.entity.CityInfoEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CityInfoRepository extends JpaRepository<CityInfoEntity, Long> {

    @Query("select entityInfo from CityInfoEntity entityInfo where entityInfo.id = ?1 ")
    CityInfoEntity getInfoByCityId(Long id);

    Page<CityInfoEntity> findByCityContainingIgnoreCase(String city, Pageable pageable);

    Page<CityInfoEntity> findByIsFavorite(boolean isFavorite, Pageable pageable);

}
