package com.valdizz.simplealarmclock.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * Room database class.
 *
 * @author Vlad Kornev
 */
@Database(entities = [Alarm::class], version = 1)
abstract class AlarmRoomDatabase : RoomDatabase() {

    abstract fun alarmDao(): AlarmDao

    companion object {
        @Volatile
        private var INSTANCE: AlarmRoomDatabase? = null

        fun getDatabase(context: Context): AlarmRoomDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AlarmRoomDatabase::class.java,
                    "Alarm_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}