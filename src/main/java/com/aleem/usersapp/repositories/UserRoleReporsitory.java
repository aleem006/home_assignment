package com.aleem.usersapp.repositories;

import com.aleem.usersapp.entities.User;
import com.aleem.usersapp.entities.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRoleReporsitory extends JpaRepository<UserRole, Long> {
    List<UserRole> findAllByUser_IdAndUnit_Id(Long userId, Long unitId);
    List<UserRole> findByUnit_Id(Long unitId);
    boolean existsByUser(User user);
}
