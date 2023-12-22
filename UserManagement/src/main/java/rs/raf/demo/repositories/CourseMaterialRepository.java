package rs.raf.demo.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import rs.raf.demo.model.CourseMaterial;
@Repository
public interface CourseMaterialRepository extends CrudRepository<CourseMaterial, Long> {
}
