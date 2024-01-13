package rs.raf.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import rs.raf.demo.model.ErrorMessage;

import java.util.List;

@Repository
public interface ErrorMessageRepository extends JpaRepository<ErrorMessage, Long> {
    @Query(nativeQuery = true,
    value = "select * from error_message em " +
            "where em.vacuum_id in (select vacuum_id from vacuum where added_by = :userId);")
    List<ErrorMessage> findAllForUser(@Param("userId") Long userId);
}
