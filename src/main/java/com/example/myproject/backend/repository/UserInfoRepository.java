package com.example.myproject.backend.repository;

import com.example.myproject.backend.domain.entity.UserInfoEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserInfoRepository extends JpaRepository<UserInfoEntity, Long> {


    @Query("select max(entityInfo.userId) from UserInfoEntity entityInfo")
    Long findMaxUserId();

    @Query("select entityInfo from UserInfoEntity entityInfo where entityInfo.username = ?1 ")
    Optional<UserInfoEntity> getUserByUsernameLogin(String username);

    @Query("select entityInfo from UserInfoEntity entityInfo where entityInfo.username = ?1 ")
    UserInfoEntity getUserByUsername(String username);

    Page<UserInfoEntity> findByUsernameContainingIgnoreCase(String username, Pageable pageable);
}