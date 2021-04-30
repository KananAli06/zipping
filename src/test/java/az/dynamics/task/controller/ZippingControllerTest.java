package az.dynamics.task.controller;

import az.dynamics.task.model.request.CreateZipFileRequestDto;
import az.dynamics.task.model.response.CreateZipFileResponseDto;
import az.dynamics.task.model.response.ZippingInfoDto;
import az.dynamics.task.service.ZippingService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static az.dynamics.task.model.Status.COMPLETED;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Kanan
 */
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WebMvcTest(ZippingController.class)
public class ZippingControllerTest {

    private static final String ZIPPING_URL = "/zip";
    private static final String STATUS_URL = "/status";
    private static final String PATH = "Test.txt";
    private static final String INVALID_PATH = "";
    private static final String ID = "id";
    private static final Integer TASK_ID = 1;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockBean
    private ZippingService zippingService;


    @Qualifier("jacksonObjectMapper")
    @Autowired
    private ObjectMapper mapper;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext)
                .build();
    }

    @Test
    void givenValidInputWhenCreateThenReturnOk() throws Exception {
        //given
        CreateZipFileRequestDto requestDto = createZipFileRequestDto();
        CreateZipFileResponseDto responseDto = createZipFileResponseDto();

        //when
        when(zippingService.zipping(requestDto)).thenReturn(responseDto);

        //then
        mockMvc.perform(post(ZIPPING_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(convertObjectToString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(content().json(convertObjectToString(responseDto)));

        verify(zippingService, times(1)).zipping(requestDto);
    }

    @Test
    void givenInvalidInputWhenCreateThenReturnBadRequest() throws Exception {
        //given
        CreateZipFileRequestDto requestDto = createInvalidZipFileRequestDto();

        //then
        mockMvc.perform(post(ZIPPING_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(convertObjectToString(requestDto)))
                .andExpect(status().isBadRequest());

        verify(zippingService, never()).zipping(requestDto);
    }


    @Test
    void givenValidInputWhenGetZippingInfoThenReturnOk() throws Exception {
        //given
        ZippingInfoDto responseDto = createZippingInfoDto();

        //when
        when(zippingService.getZippingInfo(TASK_ID)).thenReturn(responseDto);

        //then
        mockMvc.perform(get(STATUS_URL).param(ID, String.valueOf(TASK_ID))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(responseDto)));
    }

    private String convertObjectToString(Object obj) throws JsonProcessingException {
        return mapper.writeValueAsString(obj);
    }

    private CreateZipFileRequestDto createZipFileRequestDto() {
        return CreateZipFileRequestDto.builder()
                .path(PATH)
                .build();
    }

    private CreateZipFileRequestDto createInvalidZipFileRequestDto() {
        return CreateZipFileRequestDto.builder()
                .path(INVALID_PATH)
                .build();
    }

    private CreateZipFileResponseDto createZipFileResponseDto() {
        return CreateZipFileResponseDto.builder()
                .id(TASK_ID)
                .build();
    }

    private ZippingInfoDto createZippingInfoDto() {
        return ZippingInfoDto.builder()
                .status(COMPLETED)
                .filePath(PATH)
                .build();
    }

}
