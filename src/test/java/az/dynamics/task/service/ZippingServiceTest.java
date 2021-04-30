package az.dynamics.task.service;

import az.dynamics.task.entity.Process;
import az.dynamics.task.entity.Task;
import az.dynamics.task.model.request.CreateZipFileRequestDto;
import az.dynamics.task.model.response.CreateZipFileResponseDto;
import az.dynamics.task.model.response.ZippingInfoDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static az.dynamics.task.model.Status.IN_PROGRESS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

/**
 * @author Kanan
 */
@ExtendWith(MockitoExtension.class)
public class ZippingServiceTest {

    private static final Integer TASK_ID = 1;
    private static final String PATH = "src/test/resources/Test.txt";
    private static final String NAME_WITHOUT_EXTENSION = "src/test/resources/Test";
    private static final String ZIP_PATH = "src/test/resources/Test.zip";

    @Mock
    private TaskService taskService;

    @Mock
    private ProcessService processService;

    @InjectMocks
    private ZippingService zippingService;

    @Test
    void givenTaskIdWhenGetThenReturnInfo() {
        ZippingInfoDto zippingInfoDto = getInfo();
        when(taskService.getStageOfZippingTask(TASK_ID)).thenReturn(zippingInfoDto);

        ZippingInfoDto result = zippingService.getZippingInfo(TASK_ID);

        assertThat(result.getFilePath()).isEqualTo(zippingInfoDto.getFilePath());
        assertThat(result.getStatus()).isEqualTo(zippingInfoDto.getStatus());
    }

    @Test
    void givenPathNameWhenCreateThenRemoveExtension() {
        String result = zippingService.getFilePathWithoutExtension(PATH);
        assertThat(result).isEqualTo(NAME_WITHOUT_EXTENSION);
    }

    @Test
    void givenFileNameWhenCreateThenReturnZipFileName() {
        String result = zippingService.generateZipFilePath(PATH);
        assertThat(result).isEqualTo(ZIP_PATH);
    }

    @Test
    void givenPathOfFileWhenZippingThenReturnId() {
        //given
        CreateZipFileResponseDto responseDto = createResponseDto();
        CreateZipFileRequestDto requestDto = createRequestDto();

        Process process = createProcess();
        Task task = createTask();

        //when
        when(processService.create()).thenReturn(process);
        when(taskService.create(process, PATH)).thenReturn(task);

        CreateZipFileResponseDto result = zippingService.zipping(requestDto);

        //then
        assertThat(result.getId()).isEqualTo(responseDto.getId());
    }

    ZippingInfoDto getInfo() {
        return ZippingInfoDto.builder()
                .filePath(PATH)
                .status(IN_PROGRESS)
                .build();
    }

    Process createProcess() {
        return Process.builder()
                .status(IN_PROGRESS)
                .createdDate(LocalDateTime.now())
                .build();
    }

    Task createTask() {
        return Task.builder()
                .id(TASK_ID)
                .process(createProcess())
                .actualFilePath(PATH)
                .build();
    }

    CreateZipFileResponseDto createResponseDto() {
        return CreateZipFileResponseDto.builder()
                .id(TASK_ID)
                .build();
    }

    CreateZipFileRequestDto createRequestDto() {
        return CreateZipFileRequestDto.builder()
                .path(PATH)
                .build();
    }

}
