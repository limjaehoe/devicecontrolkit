package com.androidkotlin.devicecontrolkit


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class LogAdapter : RecyclerView.Adapter<LogAdapter.LogViewHolder>() {
    private var logs: List<LogEntry> = ArrayList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LogViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_log, parent, false)
        return LogViewHolder(view)
    }

    override fun onBindViewHolder(holder: LogViewHolder, position: Int) {
        val log = logs[position]
        holder.bind(log)
    }

    override fun getItemCount(): Int {
        return logs.size
    }

    fun getLogs(): List<LogEntry> {
        return logs
    }

    fun addLog(log: LogEntry) {
        logs.add(0, log) // 최신 로그를 위에 추가
        notifyItemInserted(0)
    }

    fun setLogs(logs: List<LogEntry>) {
        this.logs = logs
        notifyDataSetChanged()
    }

    internal class LogViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val timestampText: TextView
        private val actionText: TextView
        private val valueText: TextView

        init {
            timestampText = itemView.findViewById<TextView>(R.id.logTimestamp)
            actionText = itemView.findViewById<TextView>(R.id.logAction)
            valueText = itemView.findViewById<TextView>(R.id.logValue)
        }

        fun bind(log: LogEntry) {
            timestampText.setText(log.getTimestamp())
            actionText.setText(log.getAction())
            valueText.setText(log.getValue())
        }
    }
}