package com.valdizz.simplealarmclock.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * BroadcastReceiver to management alarm events.
 *
 * @author Vlad Kornev
 */
class AlarmReceiver : BroadcastReceiver() {

    private val alarmUtils = AlarmUtils()

    override fun onReceive(context: Context, intent: Intent?) {
        val action = intent?.action ?: ""
        val time = action.substring(0, 5)
        val type = action.substring(5)
        when (type) {
            "alarm" -> {
                alarmUtils.cancelNotification(context, time)
                alarmUtils.showAlarm(context, time)
            }
            "notification" -> {
                alarmUtils.sendNotification(context, time)
                alarmUtils.setAlarm(context, time, Calendar.getInstance().timeInMillis + TimeUnit.MINUTES.toMillis(5))
            }
            "cancel" -> {
                alarmUtils.cancelNotification(context, time)
                alarmUtils.cancelAlarm(context, time)
            }
        }
    }
}