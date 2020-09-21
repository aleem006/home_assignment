package com.aleem.usersapp.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity // create roles table in database
public class Role {
    @Id // make this field an id field
    @GeneratedValue(strategy = GenerationType.IDENTITY) // provided by springboot to add auto-increment id in database table
    private Long id;
    private Long version;
    private String name;

    // default construction
    public Role() {
        // assign version 1 to each new role created
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
