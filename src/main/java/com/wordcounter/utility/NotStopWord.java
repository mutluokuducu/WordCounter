package com.wordcounter.utility;

import java.util.Arrays;
import java.util.List;
import lombok.experimental.UtilityClass;

@UtilityClass
public class NotStopWord {
  private static final String A_Z_PATTERN = "[a-zA-Z]+";
  private static final List<String> STOP_WORDS =
      Arrays.asList("is", "are", ".", "if", "the", "in", "of", "and", "a", "an", "to");

  public static boolean isNotStopWord(String word) {
    return !STOP_WORDS.contains(word) && word.matches(A_Z_PATTERN);
  }
}
