package rs.raf.demo.bootstrap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import rs.raf.demo.model.*;
import rs.raf.demo.repositories.*;

@Component
public class BootstrapData implements CommandLineRunner {

    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final CourseMaterialRepository courseMaterialRepository;
    private final TeacherRepository teacherRepository;
    private final UserRepository userRepository;
    private final VacuumRepository vacuumRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public BootstrapData(StudentRepository studentRepository, CourseRepository courseRepository, CourseMaterialRepository courseMaterialRepository, TeacherRepository teacherRepository, UserRepository userRepository, VacuumRepository vacuumRepository, PasswordEncoder passwordEncoder) {
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
        this.courseMaterialRepository = courseMaterialRepository;
        this.teacherRepository = teacherRepository;
        this.userRepository = userRepository;
        this.vacuumRepository = vacuumRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {

        System.out.println("Loading Data...");

        User admin = new User();
        admin.setUsername("admin");
        admin.setPassword(this.passwordEncoder.encode("admin"));
        admin.setEmail("admin@email.com");
        admin.setCanReadUsers(true);
        admin.setCanDeleteUsers(true);
        admin.setCanUpdateUsers(true);
        admin.setCanCreateUsers(true);
        admin.setCanSearchVacuum(true);
        admin.setCanStartVacuum(true);
        admin.setCanStopVacuum(true);
        admin.setCanDischargeVacuum(true);
        admin.setCanAddVacuum(true);
        admin.setCanRemoveVacuum(true);
        this.userRepository.save(admin);

        User user1 = new User();
        user1.setUsername("user1");
        user1.setPassword(this.passwordEncoder.encode("user1"));
        user1.setEmail("user1@email.com");
        user1.setCanReadUsers(true);
        user1.setCanSearchVacuum(true);
        user1.setCanAddVacuum(true);
        user1.setCanRemoveVacuum(true);
        this.userRepository.save(user1);

        User jelena = new User();
        jelena.setUsername("jelena");
        jelena.setPassword(this.passwordEncoder.encode("1234"));
        jelena.setEmail("jelena@email.com");
        jelena.setCanReadUsers(true);
        jelena.setCanDeleteUsers(true);
        jelena.setCanUpdateUsers(true);
        jelena.setCanCreateUsers(true);

        jelena.setCanSearchVacuum(true);
        jelena.setCanStartVacuum(true);
        jelena.setCanStopVacuum(true);
        jelena.setCanDischargeVacuum(true);
        jelena.setCanAddVacuum(true);
        jelena.setCanRemoveVacuum(true);

        Vacuum vacuum1 = new Vacuum();
        vacuum1.setName("vacuum1");
        vacuum1.setActive(true); // da li je u sistemu !
        vacuum1.setStatus(Status.OFF);

        jelena.addVacuum(vacuum1);
        this.userRepository.save(jelena);

/////////////////////////////////////////////////////////


        System.out.println("Data loaded!");
    }
}
