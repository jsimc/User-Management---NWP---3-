package rs.raf.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Entity
@Getter
@Setter
public class Vacuum {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long vacuumId;

    @Enumerated(EnumType.STRING)
    @Column
    private Status status;

    @Column
    private String name;

    @ManyToOne(/*cascade = {CascadeType.PERSIST, CascadeType.REFRESH}*/)
    @JoinColumn(name = "added_by")
    @JsonIgnore
    private User addedBy;

    @Column
    private Boolean active;

    @Temporal(TemporalType.DATE)
    @CreationTimestamp
    @Column
    private Date dateCreated;

    @Column
    private int dischargeCount;
    @Version
    private Integer version = 0;

    public int increaseDischargeCount() {
        return ++this.dischargeCount;
    }

    @Override
    public String toString() {
        return "Vacuum{" +
                "vacuumId=" + vacuumId +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vacuum vacuum = (Vacuum) o;
        return Objects.equals(vacuumId, vacuum.vacuumId) && status == vacuum.status && Objects.equals(name, vacuum.name) && Objects.equals(active, vacuum.active) && Objects.equals(version, vacuum.version);
    }

    @Override
    public int hashCode() {
        return Objects.hash(vacuumId, status, name, active, version);
    }
}
