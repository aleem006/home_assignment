package com.aleem.usersapp.entities;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity // entity annotation responsible for creating database table
public class Unit {
    @Id // make this field an id field
    @GeneratedValue(strategy = GenerationType.IDENTITY) // add auto increment to the id field
    private Long id;
    private Long version;
    private String name;

    public Unit() {
        // assign version 1 to each new unit created
        this.version = 1L;
    }

    // getters and setters
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
