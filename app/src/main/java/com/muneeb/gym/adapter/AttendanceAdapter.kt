package com.muneeb.gym.Activities

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.muneeb.gym.data.Attendance
import com.muneeb.gym.databinding.AttendanceItemBinding

class AttendanceAdapter(private val attendanceList: List<Attendance>) :
    RecyclerView.Adapter<AttendanceAdapter.AttendanceViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AttendanceViewHolder {
        val binding = AttendanceItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AttendanceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AttendanceViewHolder, position: Int) {
        val attendance = attendanceList[position]
        holder.binding.tvAttendanceDate.text = attendance.date
        holder.binding.tvAttendanceStatus.text = attendance.status
    }

    override fun getItemCount(): Int = attendanceList.size

    class AttendanceViewHolder(val binding: AttendanceItemBinding) : RecyclerView.ViewHolder(binding.root)
}
