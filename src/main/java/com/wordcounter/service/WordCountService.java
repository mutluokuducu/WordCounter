package com.wordcounter.service;

import java.io.IOException;
import java.util.Map;
import org.springframework.web.multipart.MultipartFile;

public interface WordCountService {
  Map<String, Long> countWords(String text);

  Map<String, Long> countWordsInFile(MultipartFile file) throws IOException;
}
