package com.payment.services;

import android.app.Notification;
import android.content.SharedPreferences;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;

import com.payment.utils.TextToSpeechUtil;

import java.util.Objects;

public class MyNotificationListenerService extends NotificationListenerService {

    SharedPreferences preferences = null;
    SharedPreferences.Editor editor = null;
    private static final String PREF_NAME = "MyPref";
    private static final String RECENT_TRANSACTION_KEY = "RecentTransaction";
    private static final String CURRENT_BALANCE_KEY = "CurrentBalance";

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {

        preferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        editor = preferences.edit();

        Notification notification = sbn.getNotification();
        String text = notification.extras.getString(Notification.EXTRA_TEXT);
        String packageName = sbn.getPackageName();
        String recentTransactions;
//        Log.d("Notification", "############Package Name##########: " + packageName);  //tag:Notification

        // Check for debited notifications
        if((packageName.equals("com.google.android.apps.messaging") || packageName.equals("com.android.mms") || packageName.equals("com.payment")
                || packageName.equals("com.notificationcheck")) && text != null && (Objects.requireNonNull(text).contains("Union Bank") || Objects.requireNonNull(text).contains("SBI"))) {

//            Log.d("Notification", "############Package Name##########: " + packageName);  //tag:Notification

            if (Objects.requireNonNull(text).contains("Debited") || Objects.requireNonNull(text).contains("debited")) {
                String amountInHindi;
                if(Objects.requireNonNull(text).contains("Debited")) {
                    amountInHindi = extractAmount("Debited", text);
                } else {
                    amountInHindi = extractAmount("debited", text);
                }
                String speechText = "Aapke account se " + amountInHindi + " rupee kat gaye hain.";
                recentTransactions = speechText;
                editor.putString(RECENT_TRANSACTION_KEY, recentTransactions);
                TextToSpeechUtil.getInstance(this).speakText(speechText);
            }

            // Check for credited notifications
            if (Objects.requireNonNull(text).contains("Credited") || Objects.requireNonNull(text).contains("credited")) {
                String amountInHindi;
                if(Objects.requireNonNull(text).contains("Credited")) {
                    amountInHindi = extractAmount("Credited", text);
                } else {
                    amountInHindi = extractAmount("credited", text);
                }
                String speechText = "Aapke account me " + amountInHindi + " rupee prapt hue hain.";
                recentTransactions = speechText;
                editor.putString(RECENT_TRANSACTION_KEY, recentTransactions);
                TextToSpeechUtil.getInstance(this).speakText(speechText);
            }

            if(Objects.requireNonNull(text).contains("Bal") || Objects.requireNonNull(text).contains("bal") || Objects.requireNonNull(text).contains("Balance") || Objects.requireNonNull(text).contains("balance")) {
                String amountInHindi;
                if (Objects.requireNonNull(text).contains("Bal")) {
                    amountInHindi = extractAmount("Bal", text);
                } else if (Objects.requireNonNull(text).contains("bal")) {
                    amountInHindi = extractAmount("bal", text);
                } else if (Objects.requireNonNull(text).contains("Balance")) {
                    amountInHindi = extractAmount("Balance", text);
                } else {
                    amountInHindi = extractAmount("balance", text);
                }
                String speechText = "Aapka account balance, " + amountInHindi + " rupee hain.";
                editor.putString(CURRENT_BALANCE_KEY, speechText);
            }
        }

        //Must apply all the changes in shared preferences
        editor.apply();
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        // Handle notification removal if needed
    }

    private String extractAmount(String type, String text){
        StringBuilder requiredText = new StringBuilder();
        String amount;

        int index = text.indexOf(type);
        while (!Character.isDigit(text.charAt(index))) {
            index++;
        }
        while (Character.isDigit(text.charAt(index))) {
            requiredText.append(text.charAt(index));
            index++;
            if (!Character.isDigit(text.charAt(index))) {
                break;
            }
        }
        amount = requiredText.toString();
        int amountInt = Integer.parseInt(amount);
        return TextToSpeechUtil.getInstance(getApplicationContext()).convertNumberToHindiWords(amountInt);
    }
}