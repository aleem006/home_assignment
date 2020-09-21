package com.aleem.usersapp.repositories;

import com.aleem.usersapp.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserReporsitory extends JpaRepository<User, Long> {
}
