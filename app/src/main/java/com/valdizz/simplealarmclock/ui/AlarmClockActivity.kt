package com.valdizz.simplealarmclock.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity;
import com.valdizz.simplealarmclock.R
import kotlinx.android.synthetic.main.activity_alarmclock.*

/**
 * [AlarmClockActivity] has a fragment that contains alarms.
 *
 * @author Vlad Kornev
 */
class AlarmClockActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alarmclock)
        setSupportActionBar(toolbar)
        if (savedInstanceState == null) {
            createAlarmsFragment()
        }
    }

    private fun createAlarmsFragment() {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, AlarmFragment.newInstance())
            .commit()
    }

}
