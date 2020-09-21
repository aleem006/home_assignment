package com.aleem.usersapp.entities.ui;


import java.time.LocalDateTime;


/*
* To get timestamps from request body
* It is abstraction of UserRole, as we only want to update validFrom and validTo
* and leave everything as it is. So we extract these two fields in another class
* to make sure we get datetime only and nothing eles from request body
* */
public class UserRoleUpdateUI {
    LocalDateTime validFrom;
    LocalDateTime validTo;

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
