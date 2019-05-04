package com.valdizz.simplealarmclock.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

/**
 * BroadcastReceiver to start [AlarmService] after reboot.
 *
 * @author Vlad Kornev
 */
class AlarmBootReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            AlarmService().enqueueWork(context, Intent())
        }
    }
}