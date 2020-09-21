package com.aleem.usersapp.repositories;

import com.aleem.usersapp.entities.Role;
import com.aleem.usersapp.entities.Unit;
import com.aleem.usersapp.entities.User;
import com.aleem.usersapp.entities.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
    // returns result of query in a list " select * user_roles where user_id = ? and unit_id = ? "
    List<UserRole> findAllByUser_IdAndUnit_Id(Long userId, Long unitId);
    // returns result of query in a list " select * user_roles where unit_id = ? "
    List<UserRole> findByUnit_Id(Long unitId);
    // return true if any user role exists by user given
    boolean existsByUser(User user);
    // return true if any user role exists by role given
    boolean existsByRole(Role role);
    // return true if any user role exists by unit given
    boolean existsByUnit(Unit unit);
}
