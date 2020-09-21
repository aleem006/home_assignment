package com.aleem.usersapp.controllers;

import com.aleem.usersapp.entities.UserRole;
import com.aleem.usersapp.entities.ui.UserRoleUpdateUI;
import com.aleem.usersapp.repositories.UserRoleRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/user-roles")
public class UserRoleController {

    private UserRoleRepository reporsitory;

    public UserRoleController(UserRoleRepository reporsitory) {
        this.reporsitory = reporsitory;
    }

    /*
     * Return all users
     * */
    @GetMapping
    public List<UserRole> findAll() {
        return reporsitory.findAll();
    }

    /*
    * Return all user roles of user in a unit
    * */
    @GetMapping("{userId}/{unitId}")
    public List<UserRole> findAllByUnitAndUser(@PathVariable Long userId, @PathVariable Long unitId) {
        return reporsitory.findAllByUser_IdAndUnit_Id(userId, unitId);
    }

    /*
    * Return all valid user roles of user in a unit in a given timestamp
    * */
    @GetMapping("{userId}/{unitId}/{timestamp}")
    public List<UserRole> findAllUserRolesByUserAndUnitAndTimestamp(@PathVariable Long userId, @PathVariable Long unitId, @PathVariable String timestamp) {
        LocalDateTime dateTime = LocalDateTime.parse(timestamp);
        List<UserRole> roles = new ArrayList<>();
        //find all user roles by user id and unit id and filter using foreach loop
        reporsitory.findAllByUser_IdAndUnit_Id(userId, unitId).forEach(userRole -> {
            // if user role is valid in given datetime we collect user role in list defined above
            if (userRole.isValidWithDateTime(dateTime)) {
                roles.add(userRole);
            }
        });

        return roles;
    }

    /*
    * Update user role
    * the request must be in the form of
    * {
    *   "validFrom": "2020-10-10T00:05:55",
    *   "validTo" : "2020-10-20T00:05:55"
    * }
    * */
    @PutMapping("{id}/{version}")
    public ResponseEntity update(@PathVariable Long id,@PathVariable Long version, @RequestBody UserRoleUpdateUI userRoleUpdateUI){
        // find user role from database
        UserRole role = reporsitory.getOne(id);
        if(role != null){ // if user role is found

            if(role.getVersion().equals(version)){ // match the versions
                //validFrom is required
                if(userRoleUpdateUI.getValidFrom() == null){ // if put request does not contain valid timestamps

                    return new ResponseEntity<>("ValidFrom timestamps must be provided to update the role", HttpStatus.BAD_REQUEST);
                }
                //update validFrom and validTo timestamps
                role.setValidFrom(userRoleUpdateUI.getValidFrom());
                role.setValidTo(userRoleUpdateUI.getValidTo());

                // if user role is not valid show message to user
                if(!role.isValid()){
                    return new ResponseEntity<>("Invalid timestamps", HttpStatus.BAD_REQUEST);
                }

                // update user role if all above conditions passed
                return new ResponseEntity<>(reporsitory.save(role), HttpStatus.OK);
            }else{
                // show version mismatch message
                return new ResponseEntity<>("Version mismatch", HttpStatus.BAD_REQUEST);
            }
        }

        // user role with provided id does not exists
        return new ResponseEntity<>("User role not found", HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity save(@RequestBody UserRole userRole) {

        if (userRole.getValidFrom() == null) { // if validFrom timestamp is not provided
            // set validFrom to current datetime
            userRole.setValidFrom(LocalDateTime.now());
        }
        // if validFrom is after the the validTo timestamp show exception to user
        if (userRole.getValidTo() != null && userRole.getValidFrom().isAfter(userRole.getValidTo())) {
            return new ResponseEntity<>("Invalid Timestamps provided", HttpStatus.BAD_REQUEST);
        }

        // save the user role
        return new ResponseEntity<>(reporsitory.save(userRole), HttpStatus.OK);
    }

    /*
    * Delete the user role
    * */

    @DeleteMapping("{id}/{version}")
    public ResponseEntity delete(@PathVariable Long id, @PathVariable Long version) {
        // find user role from database
        UserRole role = reporsitory.getOne(id);
        if (role != null) { // if user role found
            if (role.getVersion().equals(version)) { // if versions matched
                //delete the user role
                reporsitory.delete(role);
                // show success message
                return new ResponseEntity<>("User role deleted successfully", HttpStatus.OK);
            } else {
                // show version mismatch exception
                return new ResponseEntity<>("Version mismatched", HttpStatus.BAD_REQUEST);
            }
        } else { // user not found
            return new ResponseEntity<>("User role not found", HttpStatus.NOT_FOUND);
        }

    }


}
