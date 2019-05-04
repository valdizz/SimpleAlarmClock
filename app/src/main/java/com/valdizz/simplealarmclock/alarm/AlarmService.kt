package com.valdizz.simplealarmclock.alarm

import android.content.Context
import android.content.Intent
import androidx.core.app.JobIntentService
import com.valdizz.simplealarmclock.model.AlarmRepository
import com.valdizz.simplealarmclock.model.AlarmRoomDatabase

/**
 * Service to start alarms after reboot.
 *
 * @author Vlad Kornev
 */
class AlarmService : JobIntentService() {

    fun enqueueWork(context: Context, work: Intent) {
        enqueueWork(context, AlarmService::class.java, JOB_ID, work)
    }

    override fun onHandleWork(intent: Intent) {
        val alarmUtils = AlarmUtils()
        val alarmDao = AlarmRoomDatabase.getDatabase(applicationContext).alarmDao()
        val repository = AlarmRepository(alarmDao)
        val alarms = repository.getData()
        for (alarm in alarms) {
            alarmUtils.setAlarmNotification(applicationContext, alarm.alarm)
        }
    }

    companion object {
        const val JOB_ID = 8
    }
}