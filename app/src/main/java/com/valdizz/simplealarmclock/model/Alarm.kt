package com.valdizz.simplealarmclock.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Alarm data class.
 *
 * @author Vlad Kornev
 */
@Entity(tableName = "alarms_table")
data class Alarm (@PrimaryKey val alarm: String)