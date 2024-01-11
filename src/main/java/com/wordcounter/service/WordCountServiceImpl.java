package com.wordcounter.service;

import com.wordcounter.utility.NotStopWord;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import javax.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import scala.Tuple2;

@Service
@Slf4j
public class WordCountServiceImpl implements WordCountService {

  private static final String PUNCTUATION_PATTERN = "[^a-zA-Z ]";

  private final JavaSparkContext javaSparkContext;
  private final TranslationService translationService;

  private static final Logger LOGGER = LoggerFactory.getLogger(WordCountServiceImpl.class);

  public WordCountServiceImpl(SparkConf conf, TranslationService translationService) {
    this.javaSparkContext = new JavaSparkContext(conf);
    this.translationService = translationService;
  }

  @Override
  public Map<String, Long> countWords(String text, boolean isNeedTranslate) {
    LOGGER.info("Text countWords method started");
    if (isNeedTranslate) {
      text = translationService.translateToEnglish(text);
    }
    Pattern punctuation = Pattern.compile(PUNCTUATION_PATTERN);
    JavaRDD<String> wordsRDD =
        javaSparkContext.parallelize(
            Arrays.asList(
                punctuation.matcher(text.toLowerCase()).replaceAll("").split("\\s+")));

    return new TreeMap<>(getStringLongMap(wordsRDD));
  }

  @Override
  public Map<String, Long> countWordsInFile(MultipartFile file, boolean isNeedTranslate)
      throws IOException {
    LOGGER.info("Text countWordsInFile method started");
    String fileContent =
        new BufferedReader(new InputStreamReader(file.getInputStream()))
            .lines()
            .collect(Collectors.joining("\n"));
    LOGGER.info("Text countWordsInFile file converted to string");

    if (isNeedTranslate) {
      fileContent = translationService.translateToEnglish(fileContent);
    }

    Pattern punctuation = Pattern.compile(PUNCTUATION_PATTERN);
    JavaRDD<String> wordsRDD =
        javaSparkContext.parallelize(
            Arrays.asList(
                punctuation.matcher(fileContent.toLowerCase()).replaceAll("").split("\\s+")));
    LOGGER.info("Text countWordsInFile file converted to RDD");
    return new TreeMap<>(getStringLongMap(wordsRDD));
  }

  private Map<String, Long> getStringLongMap(JavaRDD<String> words) {
    return words
        .filter(NotStopWord::isNotStopWord)
        .mapToPair(word -> new Tuple2<>(word, 1L))
        .reduceByKey(Long::sum)
        .collectAsMap();
  }

  @PreDestroy
  public void close() {
    javaSparkContext.close();
  }
}
