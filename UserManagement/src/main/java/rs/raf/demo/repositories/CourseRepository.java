package rs.raf.demo.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import rs.raf.demo.model.Course;
@Repository
public interface CourseRepository extends CrudRepository<Course, Long> {
}
