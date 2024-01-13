package rs.raf.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import rs.raf.demo.model.Vacuum;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface VacuumRepository extends JpaRepository<Vacuum, Long> {
    @Query(nativeQuery = true,
    value = "select * from vacuum " +
            "where added_by = :userId " +
//            "where vacuum_id in (select vacuum_id from user_vacuum where user_id = :userId) " +
            "and active = true " +
            "and (:vacuumName IS NULL or name like concat('%', concat(:vacuumName, '%'))) " +
            "and (coalesce(:status, null) IS NULL or status in :status) " +
            "and (:dateFrom IS NULL or date_created >= :dateFrom) " +
            "and (:dateFrom IS NULL or :dateTo IS NULL or (date_created between :dateFrom and :dateTo));")
    List<Vacuum> findAllVacuumsForLoggedInUser(@Param("userId") Long userId,
                                               @Param("vacuumName") String vacuumName,
                                               @Param("status") List<String> status,
                                               @Param("dateFrom") LocalDate dateFrom,
                                               @Param("dateTo") LocalDate dateTo);


}
