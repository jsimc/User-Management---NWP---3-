package rs.raf.demo.model;

import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password; // mora biti unique, i not null

    @Column(nullable = false, unique = true)
    private String email;
    @Column
    private boolean canReadUsers;
    @Column
    private boolean canCreateUsers;
    @Column
    private boolean canUpdateUsers;
    @Column
    private boolean canDeleteUsers;
}
