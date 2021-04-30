package az.dynamics.task.service;

import az.dynamics.task.entity.Process;
import az.dynamics.task.exception.ProcessNotFoundException;
import az.dynamics.task.model.Status;
import az.dynamics.task.repository.ProcessRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * @author Kanan
 */
@Service
public class ProcessService {
    private final ProcessRepository processRepository;

    public ProcessService(ProcessRepository processRepository) {
        this.processRepository = processRepository;
    }

    Process create() {
        Process process = new Process();
        return processRepository.save(process);
    }

    Process update(Integer id, Status status) {
        return processRepository.findById(id).map(process -> {
            process.setStatus(status);
            process.setModifiedDate(LocalDateTime.now());
            return processRepository.save(process);
        }).orElseThrow(() -> new ProcessNotFoundException(id));
    }

}
