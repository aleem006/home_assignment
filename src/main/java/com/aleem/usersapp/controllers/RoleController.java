package com.aleem.usersapp.controllers;

import com.aleem.usersapp.entities.Role;
import com.aleem.usersapp.repositories.RoleReporsitory;
import com.aleem.usersapp.repositories.UserRoleRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/roles")
public class RoleController {

    private RoleReporsitory reporsitory;
    private UserRoleRepository userRoleRepository;
    public RoleController(RoleReporsitory reporsitory, UserRoleRepository userRoleRepository) {
        this.reporsitory = reporsitory;
        this.userRoleRepository = userRoleRepository;
    }

    /*
    * Return all roles as json array
    * */
    @GetMapping
    public List<Role> findAll(){
        return reporsitory.findAll();
    }


    /*
    * Create a new and Return it in json format
    * */
    @PostMapping
    public Role save(@RequestBody Role role){
        return reporsitory.save(role);
    }

    /*
    *
    * Update Role
    * */

    @PutMapping("{id}/{version}")
    public ResponseEntity update(@PathVariable Long id, @PathVariable Long version, @RequestBody Role role){

        // get role from database
        Role role1 = reporsitory.getOne(id);

        if(role1 != null){ // if role found

            if(role1.getVersion().equals(version)){ // if versions matched update the role and return it
                role1.setName(role.getName());
                role1.setVersion(role1.getVersion() + 1);
                return new ResponseEntity<>(reporsitory.save(role1), HttpStatus.OK);
            }else{ // show version mismatch exception
                return new ResponseEntity<>("Version Mismatch", HttpStatus.BAD_REQUEST);
            }
        }

        // show role not found exception
        return new ResponseEntity<>("Role not found", HttpStatus.NOT_FOUND);
    }


    /*
    * Delete the role if id and version match
    * */
    @DeleteMapping("{id}/{version}")
    public ResponseEntity delete(@PathVariable Long id, @PathVariable Long version){

        // get role from database
        Role role1 = reporsitory.getOne(id);

        if(role1 != null){ // if role found
            if(role1.getVersion().equals(version)){ // if versions matched delete it and show message
                if(userRoleRepository.existsByRole(role1)){
                    return new ResponseEntity<>("Cannot delete role it has user roles registered with it", HttpStatus.BAD_REQUEST);
                }
                reporsitory.delete(role1);
                return new ResponseEntity<>("Role deleted successfully", HttpStatus.OK);
            }else{ // show version mismatch exception
                return new ResponseEntity<>("Version Mismatch", HttpStatus.BAD_REQUEST);
            }
        }

        // show role not found exception
        return new ResponseEntity<>("Role not found", HttpStatus.NOT_FOUND);
    }
}
