package com.example.myproject.backend.domain.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "user_info")
public class UserInfoEntity {

    @Id
    private Long userId;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "email")
    private String email;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "dob")
    private String dob;

    @Column(name = "role")
    private String role;

    @Column(name = "active")
    private String active;

    @Column(name = "i_usr")
    private String iUsr;

    @Column(name = "i_dt")
    private LocalDate iDt;

    @Column(name = "u_usr")
    private String uUsr;

    @Column(name = "u_dt")
    private LocalDate uDt;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
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
}
