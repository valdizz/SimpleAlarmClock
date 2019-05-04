package com.valdizz.simplealarmclock.model

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData

/**
 * Room repository class.
 *
 * @author Vlad Kornev
 */
class AlarmRepository(private val alarmDao: AlarmDao) {

    val alarms: LiveData<List<Alarm>> = alarmDao.getAlarms()

    @WorkerThread
    suspend fun insert(alarm: Alarm) {
        alarmDao.insert(alarm)
    }

    @WorkerThread
    suspend fun delete(alarm: Alarm) {
        alarmDao.delete(alarm)
    }

    fun getData(): List<Alarm>  = alarmDao.getData()
}