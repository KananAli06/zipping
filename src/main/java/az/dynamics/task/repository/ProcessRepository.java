package az.dynamics.task.repository;

import az.dynamics.task.entity.Process;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Kanan
 */
@Repository
public interface ProcessRepository extends JpaRepository<Process, Integer> {
}
