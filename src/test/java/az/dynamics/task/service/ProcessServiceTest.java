package az.dynamics.task.service;

import az.dynamics.task.entity.Process;
import az.dynamics.task.exception.ProcessNotFoundException;
import az.dynamics.task.repository.ProcessRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static az.dynamics.task.model.Status.COMPLETED;
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
public class ProcessServiceTest {
    private static final Integer PROCESS_ID = 1;

    @Mock
    private ProcessRepository processRepository;

    @InjectMocks
    private ProcessService processService;

    private Process process;

    @BeforeEach
    void setup() {
        process = create();
    }


    @Test
    void createProcessWhenMethodIsCalled() {
        when(processRepository.save(any())).thenReturn(process);
        Process createdProcess = processService.create();

        assertThat(createdProcess.getStatus()).isEqualTo(process.getStatus());
    }

    @Test
    void updateProcessWhenStatusIsUpdated() {
        //given
        Process updatedProcess = updatedProcess();

        //when
        when(processRepository.findById(PROCESS_ID)).thenReturn(Optional.of(process));
        when(processRepository.save(process)).thenReturn(updatedProcess);

        Process result = processService.update(PROCESS_ID, COMPLETED);

        //then
        assertThat(result.getId()).isEqualTo(updatedProcess.getId());
        assertThat(result.getStatus()).isEqualTo(updatedProcess.getStatus());

        verify(processRepository, times(1)).save(process);
        verify(processRepository, times(1)).findById(PROCESS_ID);
    }

    Process create() {
        return Process.builder()
                .status(IN_PROGRESS)
                .createdDate(LocalDateTime.now())
                .build();
    }

    Process updatedProcess() {
        return Process.builder()
                .id(PROCESS_ID)
                .createdDate(LocalDateTime.now())
                .modifiedDate(LocalDateTime.now().plusSeconds(5))
                .status(COMPLETED)
                .build();
    }

}
