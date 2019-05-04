package com.valdizz.simplealarmclock.alarm

import android.app.*
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.valdizz.simplealarmclock.R
import java.util.*
import java.util.concurrent.TimeUnit


/**
 * Сlass with alarm management utilities.
 *
 * @author Vlad Kornev
 */
class AlarmUtils {

    fun setAlarmNotification(context: Context, time: String) {
        val intent = Intent(context, AlarmReceiver::class.java).apply {
            action = time+"notification"
        }
        val pIntent = PendingIntent.getBroadcast(context, 0, intent, 0)
        with(context.getSystemService(Context.ALARM_SERVICE) as AlarmManager) {
            val currentTime = Calendar.getInstance().timeInMillis
            val timeStartAlarm = getTime(time).timeInMillis
            val timeStartNotification = getTime(time).timeInMillis - TimeUnit.MINUTES.toMillis(5)
            if (timeStartNotification > currentTime) {
                setInexactRepeating(AlarmManager.RTC_WAKEUP, timeStartNotification, AlarmManager.INTERVAL_DAY, pIntent)
            }
            else if (timeStartAlarm > currentTime && timeStartNotification < currentTime) {
                setAlarm(context, time, timeStartAlarm)
                setInexactRepeating(AlarmManager.RTC_WAKEUP, timeStartNotification + TimeUnit.DAYS.toMillis(1), AlarmManager.INTERVAL_DAY, pIntent)
            }
            else {
                setInexactRepeating(AlarmManager.RTC_WAKEUP, timeStartNotification + TimeUnit.DAYS.toMillis(1), AlarmManager.INTERVAL_DAY, pIntent)
            }
        }
    }

    fun setAlarm(context: Context, time: String, timeStart: Long) {
        val intent = Intent(context, AlarmReceiver::class.java).apply {
            action = time+"alarm"
        }
        val pIntent = PendingIntent.getBroadcast(context, 0, intent, 0)
        with(context.getSystemService(Context.ALARM_SERVICE) as AlarmManager) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, timeStart, pIntent)
            }
            else {
                setExact(AlarmManager.RTC_WAKEUP, timeStart, pIntent)
            }
        }
    }

    fun cancelAlarm(context: Context, time: String) {
        val intentAlarm = Intent(context, AlarmReceiver::class.java).apply {
            action = time+"alarm"
        }
        val pIntentAlarm = PendingIntent.getBroadcast(context, 0, intentAlarm, 0)
        val intentNotification = Intent(context, AlarmReceiver::class.java).apply {
            action = time+"notification"
        }
        val pIntentNotification = PendingIntent.getBroadcast(context, 0, intentNotification, 0)
        with(context.getSystemService(Context.ALARM_SERVICE) as AlarmManager) {
            cancel(pIntentAlarm)
            cancel(pIntentNotification)
        }
    }

    fun sendNotification(context: Context, message: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(context, CHANNEL_ID, CHANNEL_NAME)
        }
        val notification = createNotification(context, message)
        with(context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager) {
            val notificationId = message.replace(":","").toInt()
            notify(notificationId, notification)
        }
    }

    fun cancelNotification(context: Context, message: String) {
        with(context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager) {
            val notificationId = message.replace(":","").toInt()
            cancel(notificationId)
        }
    }

    fun showAlarm(context: Context, message: String) {
        Toast.makeText(context,"Alarm: "+message, Toast.LENGTH_LONG).show()
        MediaPlayer.create(context, R.raw.alarm_rington).start()
    }

    private fun createNotification(context: Context, message: String): Notification {
        val intent = Intent(context, AlarmReceiver::class.java).apply {
            action = message+"cancel"
        }
        val pIntent = PendingIntent.getBroadcast(context, 0, intent, 0)
        return NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle(context.getString(R.string.notification_title))
            .setContentText(message)
            .setSmallIcon(R.drawable.ic_alarm_black_24dp)
            .setAutoCancel(true)
            .addAction(R.drawable.ic_cancel_black_24dp, context.getString(R.string.btn_cancel), pIntent)
            .build()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(context: Context, channelId: String, channelName: String) {
        val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
        with(context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager) {
            createNotificationChannel(channel)
        }
    }

    private fun getTime(time: String) : Calendar {
        val hm = time.split(":")
        return Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hm[0].toInt())
            set(Calendar.MINUTE, hm[1].toInt())
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
    }

    companion object {
        private const val CHANNEL_ID = "com.valdizz.simplealarmclock"
        private const val CHANNEL_NAME = "Simple Alarm Clock"
    }
}