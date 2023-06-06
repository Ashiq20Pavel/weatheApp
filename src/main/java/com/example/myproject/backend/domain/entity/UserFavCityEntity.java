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

    @Column(name = "city_id")
    private Long cityId;

    @Column(name = "i_usr")
    private String iUsr;

    @Column(name = "i_dt")
    private LocalDate iDt;

    @Column(name = "u_usr")
    private String uUsr;

    @Column(name = "u_dt")
    private LocalDate uDt;

    @Column(name = "active")
    private String active;

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

    public Long getCityId() {
        return cityId;
    }

    public void setCityId(Long cityId) {
        this.cityId = cityId;
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

    public String getuUsr() {
        return uUsr;
    }

    public void setuUsr(String uUsr) {
        this.uUsr = uUsr;
    }

    public LocalDate getuDt() {
        return uDt;
    }

    public void setuDt(LocalDate uDt) {
        this.uDt = uDt;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }
}
