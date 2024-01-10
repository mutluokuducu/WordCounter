package com.wordcounter.service;

import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;
import com.wordcounter.controller.WordCountController;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TranslationServiceImpl implements TranslationService {

  @Value("${google.api.key}")
  private String googleApiKey;
  private static final Logger LOGGER = LoggerFactory.getLogger(TranslationServiceImpl.class);

  @Override
  public String translateToEnglish(String text) {
    LOGGER.info("Text word count translate started");
    Translate translate = getTranslateService();

    Translation translation =
        translate.translate(
            text,
            Translate.TranslateOption.targetLanguage("en"),
            Translate.TranslateOption.model("base"));
    return translation.getTranslatedText();
  }

  protected Translate getTranslateService() {
    return TranslateOptions.newBuilder().setApiKey(googleApiKey).build().getService();
  }
}

/**
 Translate translate =
 TranslateOptions.newBuilder().setApiKey(googleApiKey).build().getService();

 Translation translation =
 translate.translate(
 text,
 Translate.TranslateOption.targetLanguage("en"),
 Translate.TranslateOption.model("base"));
 return translation.getTranslatedText();
 }
 */