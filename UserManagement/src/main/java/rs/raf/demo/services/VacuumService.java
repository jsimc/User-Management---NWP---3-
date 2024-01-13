package rs.raf.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import rs.raf.demo.exceptions.UserException;
import rs.raf.demo.exceptions.VacuumException;
import rs.raf.demo.model.*;
import rs.raf.demo.repositories.VacuumRepository;

import java.time.*;
import java.util.*;
import java.util.concurrent.*;

@Service
public class VacuumService implements IService<Vacuum, Long> {
    private final VacuumRepository vacuumRepository;
    private final UserService userService;
    private final Set<Vacuum> usingVacuums;
    private TaskScheduler taskScheduler;
    private ErrorMessageService errorMessageService;

    @Autowired
    public VacuumService(VacuumRepository vacuumRepository, UserService userService, TaskScheduler taskScheduler, ErrorMessageService errorMessageService) {
        this.vacuumRepository = vacuumRepository;
        this.taskScheduler = taskScheduler;
        this.errorMessageService = errorMessageService;
        this.usingVacuums = new HashSet<>();
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
        Vacuum vacuum = vacuumRepository.findById(vacuumId)
            .orElseThrow(() ->
                new VacuumException(new ErrorMessage(vacuumId, "VacuumService.start(Long vacuumId)", "There is no vacuum with the id: " + vacuumId))
            );
        if(this.usingVacuums.contains(vacuum)) {
            throw new VacuumException(new ErrorMessage(vacuumId, "VacuumService.start(Long vacuumId)", "Vacuum is in use."));
        }
        if(vacuum.getStatus().equals(Status.OFF)) {
            System.out.println("Starting vacuum " + vacuum);
            this.usingVacuums.add(vacuum);
            ScheduledExecutorService executor = Executors.newScheduledThreadPool(5);
            executor.schedule(() -> {
                vacuum.setStatus(Status.ON);
                System.out.println("Vacuum " + vacuum + " is running.");
                this.usingVacuums.remove(vacuum);
                vacuumRepository.save(vacuum);
            }, 7, TimeUnit.SECONDS);
        } else {
            throw new VacuumException(
                new ErrorMessage(vacuumId, "VacuumService.start(Long vacuumId)", "Vacuum with id {" + vacuumId + "} is not OFF!")
            );
        }
        return true;
    }
    public boolean stop(Long vacuumId) throws VacuumException {
        Vacuum vacuum = vacuumRepository.findById(vacuumId)
            .orElseThrow(() ->
                new VacuumException(new ErrorMessage(vacuumId, "VacuumService.start(Long vacuumId)", "There is no vacuum with the id: " + vacuumId))
            );
        // prvo moram pitati da li se usisivac nalazi u set usingVacuums,
        // ako se nalazi onda treba da izbacim gresku! Error Handler mora da se napravi.
        if(this.usingVacuums.contains(vacuum)) {
            throw new VacuumException(new ErrorMessage(vacuumId, "VacuumService.stop(Long vacuumId)", "Vacuum is in use."));
        }
        if(vacuum.getStatus().equals(Status.ON)) {
            System.out.println("Stopping vacuum " + vacuum);
            /**
             * Dodajem usisivac u set usisivaca koji se startuju/stopiraju/discharguju, kada se to zavrsi nakon toga ih izbacujem iz tog seta.
             */
            this.usingVacuums.add(vacuum);
            ScheduledExecutorService executor = Executors.newScheduledThreadPool(5);
            executor.schedule(() -> {
                vacuum.setStatus(Status.OFF);
                System.out.println("Vacuum " + vacuum + " is stopped.");
                if(vacuum.increaseDischargeCount() % 3 == 0) {
                    try {
                        discharge(vacuumId, vacuum, true);
                    } catch (VacuumException e) {
                        throw new RuntimeException(e);
                    }
                }
                this.usingVacuums.remove(vacuum);
                vacuumRepository.save(vacuum);
            }, 7, TimeUnit.SECONDS); //TODO change to 15s
        } else {
            throw new VacuumException(new ErrorMessage(vacuumId, "VacuumService.start(Long vacuumId)", "Vacuum with id {" + vacuumId + "} is not ON!"));
        }
        return true;
    }

    public boolean discharge(Long vacuumId, Vacuum vacuumForDischarge, boolean fromStop) throws VacuumException {
        Vacuum vacuum;
        if(vacuumForDischarge != null) vacuum = vacuumForDischarge;
        else vacuum = vacuumRepository.findById(vacuumId)
            .orElseThrow(() ->
                new VacuumException(new ErrorMessage(vacuumId, "VacuumService.start(Long vacuumId)", "There is no vacuum with the id: " + vacuumId))
            );
        if(!fromStop && this.usingVacuums.contains(vacuum)) {
            throw new VacuumException(
                new ErrorMessage(vacuumId, "VacuumService.discharge(Long vacuumId, Vacuum vacuumForDischarge, boolean fromStop)", "Vacuum is in use.")
            );
        }
        if (vacuum.getStatus().equals(Status.OFF)) {
            System.out.println("Starting discharge...");
            this.usingVacuums.add(vacuum);
            ScheduledExecutorService executor = Executors.newScheduledThreadPool(5);
            executor.schedule(() -> {
                vacuum.setStatus(Status.DISCHARGING);
                System.out.println("Vacuum " + vacuum + " is discharging.");
                // TODO problem  ? kako ovo resiti
                if(false) vacuumRepository.save(vacuum);
            }, 3, TimeUnit.SECONDS); //TODO change to 15s
            executor.schedule(() -> {
//                vacuum = vacuumRepository.findById(vacuumId);
                vacuum.setStatus(Status.OFF);
                System.out.println("Vacuum " + vacuum + " is finished discharging.");
                this.usingVacuums.remove(vacuum);
                if(!fromStop) vacuumRepository.save(vacuum);
            }, 6, TimeUnit.SECONDS); //TODO change to 17s
        } else {
            throw new VacuumException(new ErrorMessage(vacuumId, "VacuumService.start(Long vacuumId)", "Cannot discharge! Vacuum with id {" + vacuumId + "} is not OFF!"));
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
        vacuum.setAddedBy(user);

        user.addVacuum(vacuum);
        userService.updateUser(user);
        return vacuum;
    }

    public boolean removeVacuum(Long vacuumId) throws VacuumException {
        Vacuum vacuum = this.vacuumRepository.findById(vacuumId)
            .orElseThrow(() ->
                new VacuumException(new ErrorMessage(vacuumId, "VacuumService.start(Long vacuumId)", "There is no vacuum with the id: " + vacuumId))
            );
        if(this.usingVacuums.contains(vacuum)) {
            throw new VacuumException(new ErrorMessage(vacuumId, "VacuumService.removeVacuum(Long vacuumId)", "Vacuum is in use."));
        }
        if(vacuum.getStatus().equals(Status.OFF)) {
            vacuum.setActive(false);
            this.vacuumRepository.save(vacuum);
            return true;
        }
        throw new VacuumException(new ErrorMessage(vacuumId, "VacuumService.start(Long vacuumId)", "Cannot remove! Vacuum with id {" + vacuumId + "} is not OFF!"));
    }

    /**
     *
     * @param vacuumId
     * @param operation start, stop, discharge
     * @param localDateTime 2024-01-11T11:15
     * @return true if successfully scheduled
     * @throws VacuumException
     */
    public boolean schedule(Long vacuumId, String operation, LocalDateTime localDateTime) throws VacuumException {
        // ukoliko je uneta operacija koja ne sme
        List<String> operations = List.of("start", "stop", "discharge");
        if(!operations.contains(operation.toLowerCase())) {
            throw new VacuumException(
                new ErrorMessage(vacuumId, "VacuumService.schedule(Long vacuumId, String operation, LocalDateTime localDateTime)", "Bad operation: " + operation)
            );
        }
        Vacuum vacuum = this.vacuumRepository.findById(vacuumId)
            .orElseThrow(() ->
                new VacuumException(new ErrorMessage(vacuumId, "VacuumService.schedule(Long vacuumId, String operation, LocalDateTime localDateTime)", "There is no vacuum with the id: " + vacuumId))
            );

        this.taskScheduler.schedule(() -> {
            System.out.println("Getting salary...");
            switch (operation) {
                case "start":
                    try {
                        this.start(vacuumId);
                    } catch (VacuumException e) {
                        if(e.getErrorMessage() != null) {
                            this.errorMessageService.save(e.getErrorMessage());
                        }
                    }
                    break;
                case "stop":
                    try {
                        this.stop(vacuumId);
                    } catch (VacuumException e) {
                        if(e.getErrorMessage() != null) {
                            this.errorMessageService.save(e.getErrorMessage());
                        }
                    }
                    break;
                case "discharge":
                    try {
                        this.discharge(vacuumId, null, false);
                    } catch (VacuumException e) {
                        if(e.getErrorMessage() != null) {
                            this.errorMessageService.save(e.getErrorMessage());
                        }
                    }
                    break;
                default:
            }
        }, localDateTime.atZone(ZoneId.of("CET")).toInstant());
        return true;
    }
    @Override
    public <S extends Vacuum> S save(S var1) {
        return null;
    }

    @Override
    public Optional<Vacuum> findById(Long id) {
        return vacuumRepository.findById(id);
    }

    @Override
    public List<Vacuum> findAll() {
        return null;
    }

    @Override
    public void deleteById(Long var1) {

    }


}
