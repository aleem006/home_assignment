package com.aleem.usersapp.repositories;

import com.aleem.usersapp.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleReporsitory extends JpaRepository<Role, Long> {
}
