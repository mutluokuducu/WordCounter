package com.wordcounter.config;

import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import org.apache.spark.SparkConf;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WordCountConfig {

  @Bean
  public SparkConf sparkConf() {
    return new SparkConf()
        .setAppName("WordCounter")
        .setMaster("local[2]"); // Use local mode with 2 threads
  }

  @Bean
  public Translate googleTranslate() {
    return TranslateOptions.getDefaultInstance().getService();
  }
}
