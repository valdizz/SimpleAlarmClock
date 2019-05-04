package com.valdizz.simplealarmclock.ui

import com.valdizz.simplealarmclock.model.Alarm

/**
 * Interface for listening to long click events on RecyclerView.
 *
 * @author Vlad Kornev
 */
interface AlarmListener {

    fun onLongClick(alarm: Alarm)
}