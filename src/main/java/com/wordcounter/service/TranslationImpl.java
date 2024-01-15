package com.wordcounter.service;

import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TranslationImpl implements Translation {

  @Value("${google.api.key}")
  private String googleApiKey;

  @Override
  public String translateToEnglish(String text) {
    log.info("Text word count translate started");
    Translate translate = getTranslateService();

    com.google.cloud.translate.Translation translation =
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