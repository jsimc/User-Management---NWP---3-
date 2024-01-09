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
import java.util.*;

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
    public ResponseEntity<?> startVacuum(@PathVariable Long vacuumId) throws VacuumException {
        return ResponseEntity.ok(Map.of("response", vacuumService.start(vacuumId)));
    }

    @PostMapping(value = "/stop/{vacuumId}")
    public ResponseEntity<?> stopVacuum(@PathVariable Long vacuumId) throws VacuumException {
        return ResponseEntity.ok().body(Map.of("response", vacuumService.stop(vacuumId)));
    }

    @PostMapping(value = "/discharge/{vacuumId}")
    public ResponseEntity<?> dischargeVacuum(@PathVariable Long vacuumId) throws VacuumException {
        return ResponseEntity.ok(Map.of("response", vacuumService.discharge(vacuumId, null, false)));
    }

    // add
    @PostMapping("/add")
    public ResponseEntity<Vacuum> createVacuum(@RequestParam(name = "name") String name) {
        try {
            return new ResponseEntity<>(vacuumService.addVacuum(name), HttpStatus.OK);
        } catch (UserException e) {
            System.out.println("user exception: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    // remove
    @PutMapping("/remove/{vacuumId}")
    public ResponseEntity<?> removeVacuum(@PathVariable Long vacuumId) throws VacuumException {
        return new ResponseEntity<>(vacuumService.removeVacuum(vacuumId), HttpStatus.OK);
    }

    // schedule a vacuum task
    @PostMapping("/schedule")
    public ResponseEntity<?> scheduleVacuumTask(
        @RequestParam(name = "dateTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime localDateTime,
        @RequestParam(name = "operation") String operation,
        @RequestParam(name = "vacuumId") Long vacuumId
    ) throws VacuumException {
        return new ResponseEntity<>(vacuumService.schedule(vacuumId, operation, localDateTime), HttpStatus.OK);
    }

}
