package com.example.basicnotificationexample

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.core.app.NotificationCompat
import androidx.core.app.RemoteInput


/**
 * Here we will demonstrate a basic example of how we can write code to make notifications
 * show on the screen. Notifications have many functionalities.
 *
 * SDK must be at least Oreo - 26 to implement most of these functionalities.
 *
 * We will show basic notifications, actions, and how a user can reply to a notification
 * without having to go to the actual app.
 *
 * Each part I finished in order will have a "Part" in the comment section before it.
 */
class MainActivity : AppCompatActivity() {

    /**
     * Must first create a notification channel before we can make a notification display.
     *
     * The channel is typically the package of your application and channelID number.
     */
    private val channelID = "com.example.notificationsdemo.channel1"

    /**
     * After that, a required NotificationManager instance must be created.
     */
    private var notificationManager: NotificationManager? = null

    /**
     * Defining a key for the reply text so the user can reply to a notification.
     * This key will also be used to receive the user's input.
     */
    private val KEY_REPLY = "key_reply"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /**
         * Now we get an instance of notificationManager.
         */
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE)
                as NotificationManager

        /**
         * Calling our createNotificationChannel() function that we created further down.
         */
        createNotificationChannel(channelID, "Demo", "Just a test Demo.")


        /**
         * Set our button to an onClickListener to display notification.
         */
        val button: Button = findViewById(R.id.button)
        button.setOnClickListener {
            displayNotification()
        }
    }

    /**
     * ---------------------------------------------------------------------------------------------
     * --------------------------- METHODS IMPLEMENTED ---------------------------------------------
     * Part1:
     *
     * Here we will define a Method to display the notification.
     *
     * In its most basic form, it displays an icon, title, and description.
     *
     * Here however, we just give the method a name and create an ID.
     */
    private fun displayNotification() {
        val notificationId = 1

        /**
         *
         * Part 2:
         *
         * We will show how to allow a user to tap on a notification that
         * will go to another UI controller. In this case, an activity.
         *
         * To do this, we will need an intent instance and a PendingIntent
         * instance.
         *
         * PendingIntents are used when we want to use the intent in the future.
         *
         * We want our pending intent below to go to our tap activity, so we set it.
         *
         * This Flag example updates the current intent if it's already in memory.
         */
        val tapIntent = Intent(this, TapActivity::class.java)
        val pendingIntent: PendingIntent = PendingIntent.getActivity(
            this,
        0,
        tapIntent,
        PendingIntent.FLAG_UPDATE_CURRENT
        )

        /**
         * Part 3
         *
         * Reply action Example that lets the user reply.
         *
         * We have to create a remote input instance with the Reply key and the reply
         * label. Be sure to import the androidx package.
         *
         * setLabel creates the label.
         *
         * Then we have to create the action.
         */
        val remoteInput: RemoteInput = RemoteInput.Builder(KEY_REPLY).run {
            setLabel("Insert the text You want to display.")
                .build()
        }

        val replyAction: NotificationCompat.Action =
            NotificationCompat.Action.Builder(0,
            "Reply Here: ",
            pendingIntent
            ).addRemoteInput(remoteInput)
                .build()

        /**
         * Part 4
         *
         * We will demo how to use Action Buttons.
         *
         * The intent setup is the same. We will just change a few variables.
         *
         * However, now we have to create an action for each intent.
         */
        val secondIntent = Intent(this, ActionButtonOne::class.java)
        val pendingIntentTwo: PendingIntent = PendingIntent.getActivity(
            this,
            0,
            secondIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        val secondAction: NotificationCompat.Action =
            NotificationCompat.Action.Builder(0,
                "Cloud Storage", pendingIntentTwo)
                .build()


        val thirdIntent = Intent(this, ActionButtonTwo::class.java)
        val pendingIntentThree: PendingIntent = PendingIntent.getActivity(
            this,
            0,
            thirdIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        val thirdAction: NotificationCompat.Action =
            NotificationCompat.Action.Builder(1,
                "Media", pendingIntentThree)
                .build()

        /**
         *
         * Part 5
         * Now we must add the pending intent to the notification instance.
         *
         * We use NotificationCompat to create everything.
         *
         * setContentTitle adds a title. setContentText adds the description.
         * setSmallIcon gives it a small icon.
         *
         * setAutoCancel automatically cancels it if set to true.
         *
         * setPriority sets the priority of importance.
         *
         * setContentIntent sets the pendingIntent.
         *
         * addAction adds an acation button.
         *
         * Lastly, we build() everything.
         */
        val notification = NotificationCompat.Builder(this@MainActivity, channelID)
            .setContentTitle("WARNING!!!!!")
            .setContentText("To go to the next Activity, click Here!")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent) // notification intent
            .addAction(secondAction) // first action intent
            .addAction(thirdAction) // second action intent
            .addAction(replyAction)
            .build()
        /**
         * Now we notify the user by passing in the variables we created above.
         */
        notificationManager?.notify(notificationId, notification)
    }

    /**
     * This was seen above.
     *
     * This is how we create the channel for the notifications.
     *
     * A notification channel has an ID, name, and description.
     *
     * We create a notification channel by creating a NotificationChannel instance and then
     * passing that instance to the createNotificationChannel() of the NotificationManager
     * class.
     */
    private fun createNotificationChannel(id: String,
                                          name: String,
                                          chDescription: String) {
        /**
         * SDK should be 8 or above. We need to write a validation to check that
         * to avoid unnecessary app crashes for lower versions.
         *
         * Importance level determines how to interrupt the user for any notification
         * that belongs to this channel.
         *
         * Then we create a channel instance and set our description.
         */
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(id,name,importance).apply {
                description = chDescription
            }
            /**
             * Now we are going to register the notification channel with the system
             * using the notification manager instance.
             */
            notificationManager?.createNotificationChannel(channel)
        } // end if statement
    }
}