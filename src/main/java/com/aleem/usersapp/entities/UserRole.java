package com.aleem.usersapp.entities;


import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class UserRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long version;
    @ManyToOne
    private User user;
    @ManyToOne
    private Unit unit;
    @ManyToOne
    private Role role;

    private LocalDateTime validFrom;
    private LocalDateTime validTo;

    public UserRole() {
        this.version = 1L;
    }

    @Transient // transient make field or method non persistent. It instructs springboot not to create database field
    public boolean isValid(){
        LocalDateTime now = LocalDateTime.now();
        // reusing the method defined below
        return isValidWithDateTime(now);
    }

    /*
    * @return boolean
    * */
    @Transient
    public boolean isValidWithDateTime(LocalDateTime dateTime){
        if(dateTime.isAfter(validFrom) && validTo == null){
            // if datetime provided is after validFrom timestamp and validTo timestamp is null then role is valid
            return true;
        }else {
            //if datetime provided is after validFrom timestamp and before validTo timestamp then role is valid otherwise it is invalid
            return dateTime.isAfter(validFrom) && dateTime.isBefore(validTo);
        }
    }


    //getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public LocalDateTime getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(LocalDateTime validFrom) {
        this.validFrom = validFrom;
    }

    public LocalDateTime getValidTo() {
        return validTo;
    }

    public void setValidTo(LocalDateTime validTo) {
        this.validTo = validTo;
    }
}
