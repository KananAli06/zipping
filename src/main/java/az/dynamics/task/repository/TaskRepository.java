package az.dynamics.task.repository;

import az.dynamics.task.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Kanan
 */
@Repository
public interface TaskRepository extends JpaRepository<Task, Integer> {
}
