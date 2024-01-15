package com.wordcounter.controller;

import static com.wordcounter.constant.Constants.POST_WORD_COUNT_URL;
import static com.wordcounter.constant.Constants.UPLOAD_FILE;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.wordcounter.service.WordCountService;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
class WordCountControllerTest {

  private MockMvc mockMvc;

  @Mock
  private WordCountService wordCountService;

  @InjectMocks
  private WordCountController wordCountController;

  @BeforeEach
  public void setup() {
    mockMvc = MockMvcBuilders.standaloneSetup(wordCountController).build();
  }

  @Test
  void countWordsTest() throws Exception {
    String testText = "Hello world";
    Map<String, Long> mockResponse = new HashMap<>();
    mockResponse.put("hello", 1L);
    mockResponse.put("world", 1L);

    given(wordCountService.countWords(anyString(), anyBoolean())).willReturn(mockResponse);

    mockMvc
        .perform(
            post(POST_WORD_COUNT_URL) // Replace with your actual URL
                .contentType(MediaType.TEXT_PLAIN)
                .content(testText))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.hello").value(1))
        .andExpect(jsonPath("$.world").value(1));
  }

  @Test
  void shouldUploadFile() throws Exception {
    MockMultipartFile file =
        new MockMultipartFile("file", "filename.txt", MediaType.MULTIPART_FORM_DATA_VALUE,
            "Hello World".getBytes());

    when(wordCountService.countWordsInFile(any(), anyBoolean())).thenReturn(
        Map.of("hello", 1L, "world", 1L));
    mockMvc
        .perform(
            multipart("/v1/upload").file(file).contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
        .andExpect(status().isOk());

    verify(wordCountService).countWordsInFile(any(), anyBoolean());
  }

  @Test
  void shouldReturnBadRequestForEmptyFile() throws Exception {
    MockMultipartFile emptyFile =
        new MockMultipartFile("files", "", "text/plain", new byte[0]);
    mockMvc
        .perform(
            multipart(UPLOAD_FILE).file(emptyFile).contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
        .andExpect(status().isBadRequest());
  }
}
