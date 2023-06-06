package com.example.myproject.backend.domain.entity;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "user_fav_city")
public class UserFavCityEntity {

    @Id
    private Long userFavCityId;

    @Column(name = "user_id")
    private Long userId;

    @ManyToOne()
    @JoinColumn(name = "city_id")
    private CityInfoEntity cityInfo;

    @Column(name = "i_usr")
    private String iUsr;

    @Column(name = "i_dt")
    private LocalDate iDt;

    public Long getUserFavCityId() {
        return userFavCityId;
    }

    public void setUserFavCityId(Long userFavCityId) {
        this.userFavCityId = userFavCityId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public CityInfoEntity getCityInfo() {
        return cityInfo;
    }

    public void setCityInfo(CityInfoEntity cityInfo) {
        this.cityInfo = cityInfo;
    }

    public String getiUsr() {
        return iUsr;
    }

    public void setiUsr(String iUsr) {
        this.iUsr = iUsr;
    }

    public LocalDate getiDt() {
        return iDt;
    }

    public void setiDt(LocalDate iDt) {
        this.iDt = iDt;
    }

}
