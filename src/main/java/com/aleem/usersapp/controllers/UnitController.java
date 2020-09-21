package com.aleem.usersapp.controllers;

import com.aleem.usersapp.entities.Unit;
import com.aleem.usersapp.repositories.UnitReporsitory;
import com.aleem.usersapp.repositories.UserRoleRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/units")
public class UnitController {

    private UnitReporsitory reporsitory;
    private UserRoleRepository userRoleRepository;

    public UnitController(UnitReporsitory reporsitory, UserRoleRepository userRoleRepository) {
        this.reporsitory = reporsitory;
        this.userRoleRepository = userRoleRepository;
    }

    /*
    * Return all units
    * */
    @GetMapping
    public List<Unit> findAll(){
        return reporsitory.findAll();
    }

    /*
     * Create a new unit and Return it in json format
     * */
    @PostMapping
    public Unit save(@RequestBody Unit unit){
        return reporsitory.save(unit);
    }

    /*
     *
     * Update Unit
     * */

    @PutMapping("{id}/{version}")
    public ResponseEntity update(@PathVariable Long id, @PathVariable Long version, @RequestBody Unit unit){

        // get unit from database
        Unit unit1 = reporsitory.getOne(id);

        if(unit1 != null){ // if unit found

            if(unit1.getVersion().equals(version)){ // if versions matched update the unit and return it
                unit1.setName(unit.getName());
                unit1.setVersion(unit.getVersion() + 1);
                return new ResponseEntity<>(reporsitory.save(unit1), HttpStatus.OK);
            }else{ // show version mismatch exception
                return new ResponseEntity<>("Version Mismatch", HttpStatus.BAD_REQUEST);
            }
        }

        // show unit not found exception
        return new ResponseEntity<>("Unit not found", HttpStatus.NOT_FOUND);
    }


    /*
     * Delete the unit if id and version match
     * */
    @DeleteMapping("{id}/{version}")
    public ResponseEntity delete(@PathVariable Long id, @PathVariable Long version){

        // get unit from database
        Unit unit1 = reporsitory.getOne(id);

        if(unit1 != null){ // if unit found
            if(unit1.getVersion().equals(version)){ // if versions matched delete it and show message
                if(userRoleRepository.existsByUnit(unit1)){
                    return new ResponseEntity<>("Cannot delete unit it has roles registered with it", HttpStatus.BAD_REQUEST);
                }
                reporsitory.delete(unit1);
                return new ResponseEntity<>("Unit deleted successfully", HttpStatus.OK);
            }else{ // show version mismatch exception
                return new ResponseEntity<>("Version Mismatch", HttpStatus.BAD_REQUEST);
            }
        }

        // show unit not found exception
        return new ResponseEntity<>("Unit not found", HttpStatus.NOT_FOUND);
    }
}
