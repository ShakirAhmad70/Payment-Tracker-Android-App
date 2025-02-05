package com.payment.activities;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.payment.R;
import com.payment.services.MyNotificationListenerService;
import com.payment.utils.TextToSpeechUtil;


public class MainActivity extends AppCompatActivity {

    private static final int NOTIFICATION_ID = 1;
    String notificationText = "";
    SharedPreferences preferences = null;
    private static final String PREF_NAME = "MyPref";
    private static final String RECENT_TRANSACTION_KEY = "RecentTransaction";
    private static final String CURRENT_BALANCE_KEY = "CurrentBalance";

    AppCompatButton triggerDebitButton, triggerCreditButton, recentTransactionButton, checkBalanceButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Adjust padding for system bars (for UI purposes)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        preferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        final String[] recentTransactions = {preferences.getString(RECENT_TRANSACTION_KEY, "Abhi tak koi transaction nahi hua hain.")};
        final String[] currentBalance = {preferences.getString(CURRENT_BALANCE_KEY, "Abhi Balance nahi bataya ja sakta hain.")};

        // Find the id's
        triggerDebitButton = findViewById(R.id.triggerDebitButton);
        triggerCreditButton = findViewById(R.id.triggerCreditButton);
        recentTransactionButton = findViewById(R.id.recentTransaction);
        checkBalanceButton = findViewById(R.id.checkBalance);

        // Initially disable the buttons
//        triggerDebitButton.setEnabled(false);
//        triggerCreditButton.setEnabled(false);
//        recentTransactionButton.setEnabled(false);
//        checkBalanceButton.setEnabled(false);

//         Check if TTS is initialized and enable the button when it's ready
//        TextToSpeechUtil.getInstance(this); // Make sure TTS is initialized

//        // Wait until TTS initialization is done before enabling the button
//        new Thread(() -> {
//            while (!TextToSpeechUtil.getInstance(MainActivity.this).isTtsInitialized()) {
//                try {
//                    //noinspection BusyWait
//                    Thread.sleep(100); // Wait for initialization
//                } catch (InterruptedException e) {
//                    throw new RuntimeException(e);
//                }
//            }
//            runOnUiThread(() -> {
//                // Enable the button once TTS is initialized
//                triggerDebitButton.setEnabled(true);
//                triggerCreditButton.setEnabled(true);
//                recentTransactionButton.setEnabled(true);
//                checkBalanceButton.setEnabled(true);
//            });
//        }).start();


        // Button to trigger notification manually
        triggerDebitButton.setOnClickListener(v -> triggerNotification("Debited"));
        triggerCreditButton.setOnClickListener(v -> triggerNotification("Credited"));

        // Button to check recent transactions
        recentTransactionButton.setOnClickListener(v -> {
            recentTransactions[0] = preferences.getString(RECENT_TRANSACTION_KEY, "Abhi tak koi transaction nahi hua hain.");
            TextToSpeechUtil.getInstance(this).speakText(recentTransactions[0]);
        });

        // Button to check balance
        checkBalanceButton.setOnClickListener(v -> {
            currentBalance[0] = preferences.getString(CURRENT_BALANCE_KEY, "Abhi Balance nahi bataya ja sakta hain.");
            TextToSpeechUtil.getInstance(this).speakText(currentBalance[0]);
        });

        // Check if the user has enabled notification access
        if (!isNotificationAccessEnabled()) {
            // If not enabled, redirect to the settings screen
            Intent intent = new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS);
            startActivity(intent);
            Toast.makeText(this, "Please enable notification access", Toast.LENGTH_LONG).show();
        } else {
            // Start the MyNotificationListenerService if notification access is enabled
            Intent serviceIntent = new Intent(this, MyNotificationListenerService.class);
            startService(serviceIntent);
        }
    }

    private boolean isNotificationAccessEnabled() {
        // Check if the notification listener service is enabled
        String enabledListeners = android.provider.Settings.Secure.getString(getContentResolver(),
                "enabled_notification_listeners");
        return enabledListeners != null && enabledListeners.contains(getPackageName());
    }

    // Trigger Notification Manually (Debit or Credit)
    private void triggerNotification(String type) {
        if (type.equals("Debited")) {
            notificationText = "your acc ****1234 is Debited by Rs. 100.0 on 01/01/2024 from Union Bank of India and current Bal is 0.0 rupee";
        } else if (type.equals("Credited")) {
            notificationText = "your acc ****1234 is Credited by Rs. 100.0 on 01/01/2024 from Union Bank of India and current Bal is 100.0 rupee";
        }

        // Get the system's Notification Manager
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // If Android Oreo or higher, use a notification channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "default_channel";
            CharSequence channelName = "Default Channel";
            String channelDescription = "Channel for account Debit and Credit notifications";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            // Create notification channel
            NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
            channel.setDescription(channelDescription);

            // Register the channel with the system
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }

            // Create notification with the channel ID
            Notification.Builder builder = new Notification.Builder(this, channelId)
                    .setContentTitle("Account " + type.substring(0, 1).toUpperCase() + type.substring(1))
                    .setContentText(notificationText)
                    .setSmallIcon(android.R.drawable.ic_dialog_info) // Replace with your own icon if needed
                    .setAutoCancel(true); // Dismiss notification when tapped

            // Show the notification
            if (notificationManager != null) {
                notificationManager.notify(NOTIFICATION_ID, builder.build());
            } else {
                Toast.makeText(this, "Error displaying notification", Toast.LENGTH_SHORT).show();
            }
        } else {
            // For devices below Android O, use the old builder method
            Notification.Builder builder = new Notification.Builder(this)
                    .setContentTitle("Account " + type.substring(0, 1).toUpperCase() + type.substring(1))
                    .setContentText(notificationText)
                    .setSmallIcon(android.R.drawable.ic_dialog_info) // Replace with your own icon if needed
                    .setAutoCancel(true); // Dismiss notification when tapped

            // Show the notification
            if (notificationManager != null) {
                notificationManager.notify(NOTIFICATION_ID, builder.build());
            } else {
                Toast.makeText(this, "Error displaying notification", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        TextToSpeechUtil.getInstance(this).shutdown();
    }
}