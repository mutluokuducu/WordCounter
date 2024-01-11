package com.wordcounter.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Map;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

class WordCountServiceTest {

  @Mock
  private JavaSparkContext javaSparkContext;

  @Mock
  private TranslationService translationService;

  private WordCountServiceImpl wordCountService;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
    SparkConf conf = new SparkConf().setAppName("test").setMaster("local");
    wordCountService = new WordCountServiceImpl(conf, translationService);
  }

  @AfterEach
  public void tearDown() {
    wordCountService.close();
  }

  @Test
  void testCountWords() {
    String testInput = "hello world, hello!, the quick brown fox. The brown dog.";
    when(translationService.translateToEnglish(testInput)).thenReturn(testInput);
    Map<String, Long> wordCounts = wordCountService.countWords(testInput, false);

    assertEquals(6, wordCounts.size());
    assertEquals(Long.valueOf(2), wordCounts.get("hello"));
    assertEquals(Long.valueOf(1), wordCounts.get("world"));
    assertEquals(2, wordCounts.getOrDefault("brown", 0L));
    assertEquals(1, wordCounts.getOrDefault("quick", 0L));
    assertEquals(1, wordCounts.getOrDefault("fox", 0L));
    assertEquals(1, wordCounts.getOrDefault("dog", 0L));

    assertNull(wordCounts.get("fox."));
    assertNull(wordCounts.get("dog."));

    assertNull(wordCounts.get("is"));
    assertNull(wordCounts.get("are"));
  }

  @Test
  void testCountWordsWithEmptyText() {
    String testText = "";
    when(translationService.translateToEnglish(testText)).thenReturn(testText);
    Map<String, Long> wordCounts = wordCountService.countWords(testText, false);

    assertTrue(wordCounts.isEmpty(), "Word count should be empty for empty text");
  }

  @Test
  void testCountWordsWithMixedCase() {
    String testText = "Apple apple APpLE";
    when(translationService.translateToEnglish(testText)).thenReturn(testText);
    Map<String, Long> wordCounts = wordCountService.countWords(testText, true);

    assertEquals(
        3, wordCounts.getOrDefault("apple", 0L), "Mixed case words should be counted correctly");
    verify(translationService).translateToEnglish(testText);
  }

  @Test
  void testCountWordsWithNumbers() {
    String testText = "Test 123 test 456";
    when(translationService.translateToEnglish(testText)).thenReturn(testText);
    Map<String, Long> wordCounts = wordCountService.countWords(testText, true);

    assertEquals(
        2, wordCounts.getOrDefault("test", 0L), "Words with numbers should be handled correctly");
    assertNull(wordCounts.get("123"), "Numbers should not be counted as words");
    verify(translationService).translateToEnglish(testText);
  }

  @Test
  void testCountWordsInFile() throws IOException {
    MultipartFile mockFile = new MockMultipartFile("file", "hello world, hello".getBytes());

    when(translationService.translateToEnglish(anyString())).thenReturn("hello world hello");
    Map<String, Long> wordCounts = wordCountService.countWordsInFile(mockFile, true);
    assertEquals(2, wordCounts.getOrDefault("hello", 0L));
    assertEquals(1, wordCounts.getOrDefault("world", 0L));
    verify(translationService).translateToEnglish(anyString());
  }
}
