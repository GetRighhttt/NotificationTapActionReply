package com.example.basicnotificationexample

import android.app.Notification
import android.app.NotificationManager
import android.app.RemoteInput
import android.content.Context
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat

/**
 * This will be the second activity that will launch when the user taps
 * on the notification, or responds to the reply.
 *
 * We will write code to receive the UserInput.
 *
 * And also write code to update the notification with the same channelID
 * and notificationID we used in the main activity.
 */
class TapActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tapp)
        receiveUserInput()
    }

    /**
     * Method to receive the input from the reply, and update the notification about it.
     */
    private fun receiveUserInput() {

        /**
         * Same key value from the main activity to pass to our remote instance.
         * And channelId, and notificationID.
         */
        val KEY_REPLY = "key_reply"
        val channelID = "com.example.notificationsdemo.channel1"
        val notificationId = 1

        /**
         * Get the current intent, then use it to get the remote input.
         *
         * The remote input instance should not be null.
         *
         * And then pass in the key value.
         */
        val intent = this.intent
        val remoteInput = RemoteInput.getResultsFromIntent(intent)
        if(remoteInput != null) {
            val inputString = remoteInput.getCharSequence(KEY_REPLY).toString()

            /**
             * Now get the textview from the activity to display the reply.
             */
            val textView: TextView = findViewById(R.id.tv_one)
            textView.text = inputString

            /**
             * Now we use the channelId, and notificationId to update the notification with
             * the reply.
             *
             * We do this the same way as in Main with less content essentially.
             *
             * Then we get the notificationManager instance, and notify the user of the update.
             */

            val replyToNotification = NotificationCompat.Builder(
                this@TapActivity, channelID)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentText("Thank you! Your reply has been received! ")
                .build()

            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.notify(notificationId, replyToNotification)
        }
    }
}