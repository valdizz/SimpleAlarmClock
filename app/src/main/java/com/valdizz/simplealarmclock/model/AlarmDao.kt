package com.valdizz.simplealarmclock.model

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

/**
 * Room DAO class.
 *
 * @author Vlad Kornev
 */
@Dao
interface AlarmDao {

    @Query("SELECT * from alarms_table")
    fun getAlarms(): LiveData<List<Alarm>>

    @Insert
    suspend fun insert(alarm: Alarm)

    @Delete
    suspend fun delete(alarm: Alarm)

    @Query("SELECT * from alarms_table")
    fun getData(): List<Alarm>
}