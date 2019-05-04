package com.valdizz.simplealarmclock.ui

import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.valdizz.simplealarmclock.R
import com.valdizz.simplealarmclock.adapter.AlarmRecyclerViewAdapter
import com.valdizz.simplealarmclock.model.Alarm
import com.valdizz.simplealarmclock.viewmodel.AlarmViewModel
import kotlinx.android.synthetic.main.fragment_alarms.*
import java.text.DecimalFormat
import java.util.*

/**
 * [AlarmFragment] displays a list of the alarm time.
 * Alarm time can be added by FAB and removed by long press.
 *
 * @author Vlad Kornev
 */
class AlarmFragment : Fragment() {

    private lateinit var alarmViewModel: AlarmViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        alarmViewModel = activity?.run {
            ViewModelProviders.of(this).get(AlarmViewModel::class.java)
        } ?: throw Exception("Invalid Activity")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_alarms, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val alarmAdapter = AlarmRecyclerViewAdapter(alarmListener)
        rv_alarms.layoutManager = LinearLayoutManager(activity)
        rv_alarms.addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))
        rv_alarms.adapter = alarmAdapter

        alarmViewModel.alarms.observe(this, Observer { alarms -> alarms?.let { alarmAdapter.setData(it) } })
        alarmViewModel.isAlarmExists.observe(this, Observer { isAlarmExists -> isAlarmExists?.let {
            if (isAlarmExists) {
                showMessage(getString(R.string.msg_alarm_exists))
            }
        }})

        fab.setOnClickListener { setTime() }
    }

    private fun setTime() {
        val currentTime = Calendar.getInstance()
        TimePickerDialog(activity, timeListener, currentTime.get(Calendar.HOUR_OF_DAY), currentTime.get(Calendar.MINUTE), true).show()
    }

    private val timeListener = TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
        val pattern = DecimalFormat("00")
        alarmViewModel.insert(Alarm("${pattern.format(hourOfDay)}:${pattern.format(minute)}"))
    }


    private val alarmListener = object: AlarmListener {
        override fun onLongClick(alarm: Alarm) {
            val dialog = AlertDialog.Builder(activity as Context)
                .setMessage("Delete alarm?")
                .setPositiveButton(android.R.string.ok) { _, _ ->  run {
                    alarmViewModel.delete(alarm)
                }}
                .setNegativeButton(android.R.string.cancel) {
                        dialog, _ -> dialog.cancel()
                }
                .create()
            dialog.show()
        }
    }

    private fun showMessage(message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        fun newInstance() = AlarmFragment()
    }
}