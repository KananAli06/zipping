package az.dynamics.task.service;

import az.dynamics.task.entity.Process;
import az.dynamics.task.entity.Task;
import az.dynamics.task.exception.TaskNotFoundException;
import az.dynamics.task.mapper.TaskMapper;
import az.dynamics.task.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static az.dynamics.task.model.Status.IN_PROGRESS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Kanan
 */
@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {
    private static final Integer TASK_ID = 1;
    private static final Integer PROCESS_ID = 1;
    private static final String FILE_PATH = "Test.txt";
    private static final String ZIP_FILE_PATH = "Test.zip";
    private static final LocalDateTime CREATED_DATE = LocalDateTime.now();
    private static final LocalDateTime MODIFIED_DATE = LocalDateTime.now().plusSeconds(5);

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private TaskMapper taskMapper;

    @InjectMocks
    private TaskService taskService;

    private Task task;
    private Process process;

    @BeforeEach
    void setup() {
        task = createTask();
        process = createProcess();
    }

    @Test
    void givenInputWhenCreateThenCreateWithoutZipPath() {
        //given
        Task taskToPersist = persistTask();

        //when
        when(taskRepository.save(taskToPersist)).thenReturn(task);

        Task persisted = taskService.create(process, FILE_PATH);

        //then
        assertThat(persisted.getId()).isEqualTo(1);
        assertThat(persisted.getProcess()).isEqualTo(process);
        assertThat(persisted.getZipFilePath()).isNull();
        assertThat(persisted.getActualFilePath()).isEqualTo(FILE_PATH);

        verify(taskRepository, times(1)).save(any());
    }

    @Test
    void givenValidIdWhenGetTaskThenReturnTask() {
        //when
        when(taskRepository.findById(TASK_ID)).thenReturn(Optional.of(task));

        Task returnedTask = taskService.findById(TASK_ID);

        //then
        assertThat(returnedTask).isEqualTo(task);
    }

    @Test
    void givenInvalidIdWhenGetTaskThenThrowException() {
        //when
        when(taskRepository.findById(TASK_ID)).thenReturn(Optional.empty());

        //then
        assertThatThrownBy(() -> taskService.findById(TASK_ID))
                .isInstanceOf(TaskNotFoundException.class);
    }

    @Test
    void givenValidIdAndZipPathWhenUpdateThenReturnUpdatedTask() {
        //given
        Task updatedTask = updatedTask();

        //when
        when(taskRepository.findById(TASK_ID)).thenReturn(Optional.of(task));
        when(taskRepository.save(task)).thenReturn(updatedTask);

        Task result = taskService.updatePathOfZipFile(TASK_ID, ZIP_FILE_PATH);

        //then
        assertThat(result).isEqualTo(updatedTask);

        verify(taskRepository, times(1)).save(task);
    }

    Task persistTask() {
        return Task.builder()
                .process(createProcess())
                .actualFilePath(FILE_PATH)
                .build();
    }

    Task createTask() {
        return Task.builder()
                .id(TASK_ID)
                .process(createProcess())
                .actualFilePath(FILE_PATH)
                .build();
    }

    Task updatedTask() {
        return Task.builder()
                .id(TASK_ID)
                .process(createProcess())
                .actualFilePath(FILE_PATH)
                .zipFilePath(ZIP_FILE_PATH)
                .build();
    }

    Process createProcess() {
        return Process.builder()
                .id(PROCESS_ID)
                .createdDate(CREATED_DATE)
                .modifiedDate(MODIFIED_DATE)
                .status(IN_PROGRESS)
                .build();

    }
}
