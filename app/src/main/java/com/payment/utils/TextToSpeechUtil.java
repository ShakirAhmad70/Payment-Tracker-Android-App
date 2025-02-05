package com.payment.utils;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.Log;

import java.util.Locale;

public class TextToSpeechUtil {

    private static TextToSpeechUtil instance;
    private final TextToSpeech textToSpeech;
    private boolean isTtsInitialized = false;

    private static final String[] tillNinetyNine = {
            "", "एक", "दो", "तीन", "चार", "पाँच", "छे", "सात", "आठ", "नौ", "दस", "ग्यारह", "बारह", "तेरह", "चौदह", "पन्द्रह", "सोलह", "सत्रह", "अठारह", "उन्नीस", "बीस", "इक्कीस", "बाईस", "ते-ईस", "चौबीस", "पच्चीस", "छब्बीस", "सत्ताईस", "अठाईस", "उनतीस", "तीस", "इकत्तीस", "बत्तीस", "तैंतीस", "चौतीस", "पैंतीस", "छत्तीस", "सैंतीस", "अड़तीस", "उनतालीस", "चालीस", "इकतालीस", "ब्यालीस", "तैतालीस", "चवालीस", "पैंतालीस", "छियालीस", "संतालीस", "अड़तालीस", "उनचास", "पचास", "इक्यावन", "बावन", "तिरेपन", "चौंवन", "पचपन", "छप्पन", "सत्तावन", "अठा-वन", "उनसठ", "साठ", "इकसठ", "बासठ", "तिरेसठ", "चौंसठ", "पैंसठ", "छियासठ", "सड़सठ", "अड़सठ", "उनहत्तर", "सत्तर", "इकहात्तर", "बहात्तर", "तिहत्तर", "चौहत्तर", "पिचहत्तर", "छिहत्तर", "सतत्तर", "अठहत्तर", "उनासी", "अससी", "इक्यासी", "ब्यासी", "तिरासी", "चौरासी", "पिचासी", "छियासी", "सतासी", "अठासी", "नवासी", "नब्बे", "इक्यानवे", "बानवे", "तिरानवे", "चौरानवे", "पिचानवे", "छियानवे", "सत्तानवे", "अठानवे", "निन्यानवे"
    };

    private TextToSpeechUtil(Context context) {
        textToSpeech = new TextToSpeech(context, new OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    // Set language to Hindi
                    int langResult = textToSpeech.setLanguage(Locale.forLanguageTag("hi-IN"));
                    if (langResult == TextToSpeech.LANG_MISSING_DATA
                            || langResult == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("TTS", "Hindi language is not supported or missing data.");
                    } else {
                        // TTS initialized successfully
                        isTtsInitialized = true;
                    }
                } else {
                    Log.e("TTS", "Initialization failed.");
                }
            }
        });
    }

    public static synchronized TextToSpeechUtil getInstance(Context context) {
        if (instance == null) {
            instance = new TextToSpeechUtil(context);
        }
        return instance;
    }

    // Method to check if TTS is initialized
    public boolean isTtsInitialized() {
        return isTtsInitialized;
    }

    // Method to convert number to Hindi words
    public String convertNumberToHindiWords(int number) {
        if (number == 0) {
            return "zero";
        }

        StringBuilder words = new StringBuilder();

        // Handle crore
        if (number / 10000000 > 0) {
            int croreValue = number / 10000000;
            words.append(tillNinetyNine[croreValue]).append(" karod ");
            number %= 10000000;
        }

        // Handle lakhs
        if (number / 100000 > 0) {
            int lakhValue = number / 100000;
            words.append(tillNinetyNine[lakhValue]).append("  lakh ");
            number %= 100000;
        }

        // Handle thousands
        if (number / 1000 > 0) {
            int thousandValue = number / 1000;
            words.append(tillNinetyNine[thousandValue]).append(" hajaar ");
            number %= 1000;
        }

        // Handle hundreds
        if (number / 100 > 0) {
            int hundredValue = number / 100;
            words.append(tillNinetyNine[hundredValue]).append(" sau ");
            number %= 100;
        }

        // Handle tens (1-99)
        if (number > 0 && number < 100) {
            words.append(tillNinetyNine[number]).append(" ");
        }

        return words.toString().trim();
    }

    public void speakText(String text) {
        if (textToSpeech != null) {
            textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
        }
    }

    public void stopSpeaking() {
        if (textToSpeech != null) {
            textToSpeech.stop();
        }
    }

    public void shutdown() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
    }
}