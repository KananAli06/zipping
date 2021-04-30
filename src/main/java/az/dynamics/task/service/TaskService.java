package az.dynamics.task.service;

import az.dynamics.task.entity.Process;
import az.dynamics.task.entity.Task;
import az.dynamics.task.exception.TaskNotFoundException;
import az.dynamics.task.mapper.TaskMapper;
import az.dynamics.task.model.response.ZippingInfoDto;
import az.dynamics.task.repository.TaskRepository;
import org.springframework.stereotype.Service;

/**
 * @author Kanan
 */
@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    public TaskService(TaskRepository taskRepository, TaskMapper taskMapper) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
    }

    Task create(final Process process, final String filePath) {
        Task task = new Task();
        task.setProcess(process);
        task.setActualFilePath(filePath);
        return taskRepository.save(task);
    }

    public Task findById(final Integer id) {
        return taskRepository
                .findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
    }

    Task updatePathOfZipFile(Integer id, String pathOfZipFile) {
        return taskRepository.findById(id).map(zippingInfo -> {
            zippingInfo.setZipFilePath(pathOfZipFile);
            return taskRepository.save(zippingInfo);
        }).orElseThrow(() -> new TaskNotFoundException(id));
    }

    ZippingInfoDto getStageOfZippingTask(final Integer id) {
        return taskMapper.toDto(findById(id));
    }

}
