package rs.raf.demo.controllers;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.raf.demo.dtos.UserDto;
import rs.raf.demo.exceptions.UserException;
import rs.raf.demo.exceptions.VacuumException;
import rs.raf.demo.model.User;
import rs.raf.demo.model.Vacuum;
import rs.raf.demo.services.VacuumService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/vacuum")
public class VacuumController {
    private final VacuumService vacuumService;

    public VacuumController(VacuumService vacuumService) {
        this.vacuumService = vacuumService;
    }

    @GetMapping(value = "/search")
    public ResponseEntity<List<Vacuum>> searchVacuums(
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "status", required = false) String statuses,
            @RequestParam(name = "dateFrom", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFrom,
            @RequestParam(name = "dateTo", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateTo
     ){
        List<String> statusList;
        if(statuses == null) statusList = new ArrayList<>();
        else statusList= Arrays.asList(statuses.split(","));
        return new ResponseEntity<>(vacuumService.search(name, statusList, dateFrom, dateTo), HttpStatus.OK);
    }

    @PostMapping(value = "/start/{vacuumId}")
    public ResponseEntity<?> startVacuum(@PathVariable Long vacuumId) {
        try {
            return new ResponseEntity<>(vacuumService.start(vacuumId), HttpStatus.OK);
        } catch (VacuumException vacuumException) {
            System.out.println(vacuumException.getMessage());
            return new ResponseEntity<>(vacuumException.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "/stop/{vacuumId}")
    public ResponseEntity<?> stopVacuum(@PathVariable Long vacuumId) {
        try {
            return new ResponseEntity<>(vacuumService.stop(vacuumId), HttpStatus.OK);
        } catch (VacuumException vacuumException) {
            System.out.println(vacuumException.getMessage());
            return new ResponseEntity<>(vacuumException.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "/discharge/{vacuumId}")
    public ResponseEntity<?> dischargeVacuum(@PathVariable Long vacuumId) {
        try {
            return new ResponseEntity<>(vacuumService.discharge(vacuumId, null, true), HttpStatus.OK);
        } catch (VacuumException vacuumException) {
            System.out.println(vacuumException.getMessage());
            return new ResponseEntity<>(vacuumException.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // add
    @PostMapping("/add")
    public ResponseEntity<Vacuum> createUser(@RequestParam(name = "name") String name) {
        try {
            return new ResponseEntity<>(vacuumService.addVacuum(name), HttpStatus.OK);
        } catch (UserException e) {
            System.out.println("user exception: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
    @PutMapping("/remove/{vacuumId}")
    public ResponseEntity<?> removeVacuum(@PathVariable Long vacuumId) {
        try {
            boolean removed = vacuumService.removeVacuum(vacuumId);
            if(removed){
                return new ResponseEntity<>(HttpStatus.OK);
            }
            return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error during removal of vacuum with id " + vacuumId);
        } catch (VacuumException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    // remove

}
