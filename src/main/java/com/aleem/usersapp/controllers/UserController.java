package com.aleem.usersapp.controllers;

import com.aleem.usersapp.entities.User;
import com.aleem.usersapp.repositories.UserReporsitory;
import com.aleem.usersapp.repositories.UserRoleRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("users")
public class UserController {

    private UserReporsitory reporsitory;
    private UserRoleRepository userRoleRepository;

    public UserController(UserReporsitory reporsitory, UserRoleRepository userRoleRepository) {
        this.reporsitory = reporsitory;
        this.userRoleRepository = userRoleRepository;
    }

    /*
    * Return all users
    * */
    @GetMapping
    public List<User> findAll(){
        return reporsitory.findAll();
    }

    /*
    * Return Json array of Users with atleast one valid role in a unit in a given timestamp
    * timestamp format must be yyyy-mm-ddThh:mm::ss
    * */
    @GetMapping("{unitId}/{timestamp}")
    public ResponseEntity findByUnitAndRole(@PathVariable Long unitId, @PathVariable String timestamp){
        List<User> users = new ArrayList<>(); // create a new empty list
        LocalDateTime dateTime;
        try {
            dateTime = LocalDateTime.parse(timestamp); // try to convert string to datetime
        }catch (Exception ex){
            // show exception message if datetime string is invalid
            return new ResponseEntity<>("Invalid timestamp provided", HttpStatus.BAD_REQUEST);
        }
        userRoleRepository.findByUnit_Id(unitId).forEach(userRole -> {
            if(userRole.isValidWithDateTime(dateTime)){
                users.add(userRole.getUser());
            }
        });
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @PostMapping
    public User save(@RequestBody User user){
        return reporsitory.save(user);
    }

    @PutMapping("{id}/{version}")
    public ResponseEntity update(@PathVariable Long id, @PathVariable Long version, @RequestBody User user){
        // get user from database
        User user1 = reporsitory.getOne(id);

        if(user1 != null){ // if user found
            if(user1.getVersion().equals(version)){ // if versions matched update the user
                user1.setName(user.getName());
                user1.setVersion(user.getVersion() + 1);
                return new ResponseEntity<>(reporsitory.save(user1), HttpStatus.OK);
            }else{ // show version mismatch exception
                return new ResponseEntity<>("Version Mismatch", HttpStatus.BAD_REQUEST);
            }
        }
        // show user not found exception
        return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("{id}/{version}")
    public ResponseEntity delete(@PathVariable Long id, @PathVariable Long version){
        // find the user from database
        User user = reporsitory.getOne(id);
        if(user != null){ // if user is found
            if(user.getVersion().equals(version)){ // if versions match
                if(userRoleRepository.existsByUser(user)){ // if user have roles assigned we prevent its deletion and show message to user
                    return new ResponseEntity<>("Cannot delete user. It has registered user roles", HttpStatus.BAD_REQUEST);
                }else{ // if user don't have any role, delete it and show success message
                    reporsitory.delete(user);
                    return new ResponseEntity<>("User deleted successfully", HttpStatus.OK);
                }
            }

        }

        // show message that user not found
        return new ResponseEntity<>("User not found", HttpStatus.BAD_REQUEST);
    }
}
