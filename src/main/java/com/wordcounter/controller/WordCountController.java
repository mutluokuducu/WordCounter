package com.wordcounter.controller;

import static com.wordcounter.constant.Constants.POST_WORD_COUNT_URL;
import static com.wordcounter.constant.Constants.UPLOAD_FILE;
import static com.wordcounter.exeption.ErrorType.BAD_REQUEST;
import static com.wordcounter.exeption.ErrorType.INTERNAL_ERROR;

import com.wordcounter.exeption.WordCountServiceException;
import com.wordcounter.service.WordCountService;
import io.swagger.v3.oas.annotations.Operation;
import java.io.IOException;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@CrossOrigin
@Slf4j
public class WordCountController {

  private final WordCountService wordCountService;

  @Autowired
  public WordCountController(
      WordCountService wordCountService) {
    this.wordCountService = wordCountService;
  }

  @PostMapping(value = POST_WORD_COUNT_URL)
  public ResponseEntity<Map<String, Long>> countWords(
      @RequestBody String text,
      @RequestParam(value = "isNeedTranslate", defaultValue = "false") boolean isNeedTranslate) {
    log.info("Text word count service started");
    Map<String, Long> wordCounts = wordCountService.countWords(text, isNeedTranslate);
    return ResponseEntity.ok(wordCounts);
  }

  @PostMapping(value = UPLOAD_FILE, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  @Operation(summary = "Upload a single File")
  public ResponseEntity<Map<String, Long>> uploadFile(
      @RequestParam("file") MultipartFile file,
      @RequestParam(value = "isNeedTranslate", defaultValue = "false") boolean isNeedTranslate) {

    try {
      log.info("Text word count service file uploaded {}", file.getName());
      Map<String, Long> wordCounts = wordCountService.countWordsInFile(file, isNeedTranslate);
      return ResponseEntity.ok(wordCounts);
    } catch (IOException e) {
      log.error("File upload get error {}", file.getName());
      throw new WordCountServiceException(INTERNAL_ERROR);
    }
  }
}
