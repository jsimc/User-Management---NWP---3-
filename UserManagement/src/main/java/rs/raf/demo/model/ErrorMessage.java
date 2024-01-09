package rs.raf.demo.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
public class ErrorMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    @Column
    private Date dateCreated;

    @Column
    private Long vacuumId;

    @Column
    private String operation;

    @Column
    private String errorMessage;

    public ErrorMessage() {
    }

    public ErrorMessage(Long vacuumId, String operation, String errorMessage) {
        this.vacuumId = vacuumId;
        this.operation = operation;
        this.errorMessage = errorMessage;
    }
}
