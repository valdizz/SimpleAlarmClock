package com.valdizz.simplealarmclock.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.valdizz.simplealarmclock.R
import com.valdizz.simplealarmclock.model.Alarm
import com.valdizz.simplealarmclock.ui.AlarmListener
import kotlinx.android.synthetic.main.item_alarm.view.*

/**
 * Adapter class for displaying alarms in [RecyclerView].
 *
 * @author Vlad Kornev
 */
class AlarmRecyclerViewAdapter(private val alarmListener: AlarmListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var alarms = emptyList<Alarm>()

    internal fun setData(alarms: List<Alarm>) {
        this.alarms = alarms
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_alarm, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = alarms[position]
        holder.itemView.tv_alarm.text = item.alarm
        holder.itemView.setOnLongClickListener {
            alarmListener.onLongClick(item)
            true
        }
    }

    override fun getItemCount(): Int {
        return alarms.size
    }
}