package com.wordcounter.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;

import com.google.cloud.translate.Translate;
import com.google.cloud.translate.Translation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class TranslationServiceImplTest {

  @Mock
  private Translate translate;
  @Mock
  private Translation translation;

  private TranslationServiceImpl translationService;

  @BeforeEach
  public void setUp() throws Exception {
    MockitoAnnotations.openMocks(this);
    when(translate.translate(
        anyString(),
        any(Translate.TranslateOption.class),
        any(Translate.TranslateOption.class)))
        .thenReturn(translation);
    when(translation.getTranslatedText()).thenReturn("translated text");

    translationService =
        new TranslationServiceImpl() {
          @Override
          protected Translate getTranslateService() {
            return translate;
          }
        };
  }

  @Test
  void testTranslateToEnglish() {
    String result = translationService.translateToEnglish("test text");
    assertEquals("translated text", result);
  }
}
