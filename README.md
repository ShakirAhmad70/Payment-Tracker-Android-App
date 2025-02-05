**README for Payment Notification App**
**Overview** : 
This is a simple Android application that simulates a payment system. It provides users with real-time notifications for debit and credit transactions, allows them to check recent transactions, and view their current balance. The app utilizes TextToSpeech to read the notifications and transaction details aloud.

**Features** : 
Debit/Credit Notifications: Manually trigger notifications for debit or credit transactions.
Recent Transactions: View the recent transaction history with the option to hear the details via text-to-speech.
Check Balance: View the current account balance with text-to-speech output.
Notification Listener: Listens for incoming notifications related to account transactions and updates the recent transaction and balance accordingly.


**Requirements** : 
Android Studio with the following dependencies:

TextToSpeech (for speaking the transaction details).
Notification Listener (to read notifications related to transactions).
SharedPreferences (to store transaction and balance data).
Android version: API Level 21 (Lollipop) or higher.

**App Structure** : 
MainActivity: The main user interface. It includes buttons for triggering debit/credit notifications, checking recent transactions, and checking the balance.

Notification Listener Service: A service that listens for notifications from banking apps (such as Union Bank or SBI) and updates the transaction details and balance.

**Setup and Installation** : 
Clone or download the repository to your local machine.
Open the project in Android Studio.
Ensure your app has permission to access notifications:
Navigate to your device’s settings.
Enable Notification Access for the app under the Notification Listener Settings.
Build and run the app on your emulator or physical device.

**Functionality** : 
Trigger Debit/Credit Notifications:

Click the Trigger Debit Notification button to simulate a debit notification.
Click the Trigger Credit Notification button to simulate a credit notification.
View Recent Transactions:

Click the Recent Transaction button to hear the most recent transaction made.
Check Balance:

Click the Check Balance button to hear the current balance.
Notification Listener Service:

The app listens for notifications from supported apps (e.g., Union Bank, SBI).
When a relevant notification is received, the app updates the stored transaction details and current balance.
Code Explanation
MainActivity: Handles UI elements and triggers notifications for transactions. It also checks if notification access has been granted and prompts the user to enable it if necessary.

MyNotificationListenerService: A service that listens to notifications and extracts transaction details (e.g., debit, credit) from incoming notifications. It updates the shared preferences with the latest transaction details and current balance.

TextToSpeechUtil: A utility class for text-to-speech functionality, which is used to speak the transaction and balance details.

**Permissions** : 
Notification Access: The app requires permission to access notifications to listen for incoming transaction notifications.
Known Issues
The app currently works only with certain banking apps (like Union Bank and SBI) that send notifications in a specific format.
Contribution
Contributions are welcome! Feel free to fork the repository, make changes, and submit pull requests.

