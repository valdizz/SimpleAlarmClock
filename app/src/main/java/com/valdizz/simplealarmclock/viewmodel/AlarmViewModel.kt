package com.valdizz.simplealarmclock.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.valdizz.simplealarmclock.alarm.AlarmUtils
import com.valdizz.simplealarmclock.model.Alarm
import com.valdizz.simplealarmclock.model.AlarmRepository
import com.valdizz.simplealarmclock.model.AlarmRoomDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * ViewModel class implements the logic of alarm control.
 *
 * @author Vlad Kornev
 */
class AlarmViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: AlarmRepository
    private val alarmUtils: AlarmUtils
    val alarms: LiveData<List<Alarm>>
    val isAlarmExists: MutableLiveData<Boolean>


    init {
        val alarmDao = AlarmRoomDatabase.getDatabase(application).alarmDao()
        repository = AlarmRepository(alarmDao)
        alarms = repository.alarms
        alarmUtils = AlarmUtils()
        isAlarmExists = MutableLiveData()
    }

    fun insert(alarm: Alarm)= viewModelScope.launch(Dispatchers.IO) {
        isAlarmExists.postValue(false)
        if (alarms.value?.contains(alarm) == false) {
            alarmUtils.setAlarmNotification(getApplication(), alarm.alarm)
            repository.insert(alarm)
        }
        else {
            isAlarmExists.postValue(true)
        }
    }

    fun delete(alarm: Alarm)= viewModelScope.launch(Dispatchers.IO) {
        alarmUtils.cancelAlarm(getApplication(), alarm.alarm)
        repository.delete(alarm)
    }
}