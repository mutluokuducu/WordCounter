package com.wordcounter.controller;

import static com.wordcounter.constant.Constants.GET_TRANSLATE;
import static com.wordcounter.constant.Constants.POST_WORD_COUNT_URL;
import static com.wordcounter.constant.Constants.UPLOAD_FILE;

import com.wordcounter.service.TranslationService;
import com.wordcounter.service.WordCountService;
import io.swagger.v3.oas.annotations.Operation;
import java.io.IOException;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
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

  private final TranslationService translationService;
  private static final Logger LOGGER = LoggerFactory.getLogger(WordCountController.class);

  @Autowired
  public WordCountController(
      WordCountService wordCountService, TranslationService translationService) {
    this.wordCountService = wordCountService;
    this.translationService = translationService;
  }

  @PostMapping(value = POST_WORD_COUNT_URL)
  public ResponseEntity<Map<String, Long>> countWords(
      @RequestBody String text,
      @RequestParam(value = "isNeedTranslate", defaultValue = "false") boolean isNeedTranslate) {
    LOGGER.info("Text word count service started");
    Map<String, Long> wordCounts = wordCountService.countWords(text, isNeedTranslate);
    return ResponseEntity.ok(wordCounts);
  }

  @PostMapping(value = UPLOAD_FILE, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  @Operation(summary = "Upload a single File")
  public ResponseEntity<?> uploadFile(
      @RequestParam("file") MultipartFile file,
      @RequestParam(value = "isNeedTranslate", defaultValue = "false") boolean isNeedTranslate) {
    if (file.isEmpty()) {
      return ResponseEntity.badRequest().body("file is empty");
    }
    try {
      LOGGER.info("Text word count service file uploaded {}", file.getName());
      Map<String, Long> wordCounts = wordCountService.countWordsInFile(file, isNeedTranslate);
      return ResponseEntity.ok(wordCounts);
    } catch (IOException e) {
      LOGGER.error("File upload get error {}", file.getName());
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("get error");
    }
  }

  @GetMapping(value = GET_TRANSLATE)
  public String translate(@RequestParam String text) {
    return translationService.translateToEnglish(text);
  }
}
