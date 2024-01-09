package rs.raf.demo.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;


    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String email;
    @Column
    private boolean canReadUsers;
    @Column
    private boolean canCreateUsers;
    @Column
    private boolean canUpdateUsers;
    @Column
    private boolean canDeleteUsers;

    @Column
    private boolean canSearchVacuum;
    @Column
    private boolean canStartVacuum;
    @Column
    private boolean canStopVacuum;
    @Column
    private boolean canDischargeVacuum;
    @Column
    private boolean canAddVacuum;
    @Column
    private boolean canRemoveVacuum;

    @OneToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(
        name = "user_vacuum",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "vacuum_id")
    )
    private List<Vacuum> addedVacuums = new ArrayList<>();

    public void addVacuum(Vacuum vacuum) {
        this.getAddedVacuums().add(vacuum);
        vacuum.setAddedBy(this);
    }
}
