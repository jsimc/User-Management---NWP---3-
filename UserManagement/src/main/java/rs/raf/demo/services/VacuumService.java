package rs.raf.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import rs.raf.demo.dtos.UserDto;
import rs.raf.demo.exceptions.UserException;
import rs.raf.demo.exceptions.VacuumException;
import rs.raf.demo.model.MyUserDetails;
import rs.raf.demo.model.Status;
import rs.raf.demo.model.User;
import rs.raf.demo.model.Vacuum;
import rs.raf.demo.repositories.VacuumRepository;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.*;

@Service
public class VacuumService implements IService<Vacuum, Long> {
    private final VacuumRepository vacuumRepository;
    private final UserService userService;

    private List<Vacuum> usingVacuums;

    @Autowired
    public VacuumService(VacuumRepository vacuumRepository, UserService userService) {
        this.vacuumRepository = vacuumRepository;
        this.usingVacuums = new ArrayList<>();
        this.userService = userService;
    }

    /**
     * find all vacuums that were added by logged-in user
     */
    public List<Vacuum> search(String name, List<String> status, LocalDate dateFrom, LocalDate dateTo) {
        Long loggedUserId = ((MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUserId();
        if(status == null) status = new ArrayList<>();
        return this.vacuumRepository.findAllVacuumsForLoggedInUser(loggedUserId, name, status, dateFrom, dateTo);
    }
    public boolean start(Long vacuumId) throws VacuumException{
        Vacuum vacuum = vacuumRepository.findById(vacuumId).orElseThrow(() -> new VacuumException("There is no vacuum with the id: " + vacuumId));
        if(vacuum.getStatus().equals(Status.OFF)) {
            System.out.println("Starting vacuum " + vacuum);
            ScheduledExecutorService executor = Executors.newScheduledThreadPool(5);
            executor.schedule(() -> {
                vacuum.setStatus(Status.ON);
                System.out.println("Vacuum " + vacuum + " is running.");
                vacuumRepository.save(vacuum);
            }, 3, TimeUnit.SECONDS);
        } else {
            throw new VacuumException("Vacuum with id {" + vacuumId + "} is not OFF!");
        }
        return true;
    }
    public boolean stop(Long vacuumId) throws VacuumException {
        Vacuum vacuum = vacuumRepository.findById(vacuumId).orElseThrow(() -> new VacuumException("There is no vacuum with the id: " + vacuumId));
        if(vacuum.getStatus().equals(Status.ON)) {
            System.out.println("Stopping vacuum " + vacuum);
            ScheduledExecutorService executor = Executors.newScheduledThreadPool(5);
            executor.schedule(() -> {
                vacuum.setStatus(Status.OFF);
                System.out.println("Vacuum " + vacuum + " is stopped.");
                if(vacuum.increaseDischargeCount() % 3 == 0) {
                    try {
                        discharge(vacuumId, vacuum, false);
                    } catch (VacuumException e) {
                        throw new RuntimeException(e);
                    }
                }
                vacuumRepository.save(vacuum);
            }, 3, TimeUnit.SECONDS); //TODO change to 15s
        } else {
            throw new VacuumException("Vacuum with id {" + vacuumId + "} is not ON!");
        }
        return true;
    }

    // TODO problem sa ovim je sto nakon 15 sekundi menjam na discharging
    //  i moram to da sacuvam negde i apdejtujem, mozda da imam listu svih usisivaca koji trenutno su discharging ili se startuju ili stopuju
    //  tako da imam uvid u njih bez da se konsultujem sa bazom, ali npr onda mi u bazi nece pisati discharging. Smisli to sutra.
    public boolean discharge(Long vacuumId, Vacuum vacuumForDischarge, boolean doSave) throws VacuumException {
        Vacuum vacuum;
        if(vacuumForDischarge != null) vacuum = vacuumForDischarge;
        else vacuum = vacuumRepository.findById(vacuumId).orElseThrow(() -> new VacuumException("There is no vacuum with the id: " + vacuumId));
//        this.usingVacuums.add(vacuum);
        if (vacuum.getStatus().equals(Status.OFF)) {
            System.out.println("Starting discharge...");
            ScheduledExecutorService executor = Executors.newScheduledThreadPool(5);
            executor.schedule(() -> {
                vacuum.setStatus(Status.DISCHARGING);
                System.out.println("Vacuum " + vacuum + " is discharging.");
                if(false) vacuumRepository.save(vacuum);
            }, 3, TimeUnit.SECONDS); //TODO change to 15s
            executor.schedule(() -> {
//                vacuum = vacuumRepository.findById(vacuumId);
                vacuum.setStatus(Status.OFF);
                System.out.println("Vacuum " + vacuum + " is finished discharging.");
                if(doSave) vacuumRepository.save(vacuum);
            }, 6, TimeUnit.SECONDS); //TODO change to 17s
        } else {
            throw new VacuumException("Cannot discharge! Vacuum with id {" + vacuumId + "} is not OFF!");
        }
        return true;
    }

    public Vacuum addVacuum(String name) throws UserException {
        Long loggedUserId = ((MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUserId();
        User user = userService.findUserById(loggedUserId);

        Vacuum vacuum = new Vacuum();
        vacuum.setName(name);
        vacuum.setActive(true);
        vacuum.setStatus(Status.OFF);

        user.addVacuum(vacuum);
        userService.updateUser(user);
        return null;
    }

    public boolean removeVacuum(Long vacuumId) throws VacuumException {
        Vacuum vacuum = this.vacuumRepository.findById(vacuumId).orElseThrow(() -> new VacuumException("There is no vacuum with the id: " + vacuumId));
        if(vacuum.getStatus().equals(Status.OFF)) {
            vacuum.setActive(false);
            this.vacuumRepository.save(vacuum);
            return true;
        }
        throw new VacuumException("Cannot remove! Vacuum with id {" + vacuumId + "} is not OFF!");
    }

    // TODO Schedule Start/Stop/Discharge task
    public void schedule() {
        /**
            CronTrigger cronTrigger = new CronTrigger("0 0 0 25 * *"); // "0 0 0 25 * *"
            this.taskScheduler.schedule(() -> {
                System.out.println("Getting salary...");
                this.userRepository.increaseBalance(salary, username);
            }, cronTrigger);
        */
    }
    @Override
    public <S extends Vacuum> S save(S var1) {
        return null;
    }

    @Override
    public Optional<Vacuum> findById(Long var1) {
        return Optional.empty();
    }

    @Override
    public List<Vacuum> findAll() {
        return null;
    }

    @Override
    public void deleteById(Long var1) {

    }


}
