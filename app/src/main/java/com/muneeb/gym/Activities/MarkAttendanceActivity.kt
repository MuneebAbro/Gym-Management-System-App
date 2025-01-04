package com.muneeb.gym.Activities

import android.content.ContentValues
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.muneeb.gym.Database.DatabaseHelper
import com.muneeb.gym.R
import com.muneeb.gym.data.Attendance
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MarkAttendanceActivity : AppCompatActivity() {
    private lateinit var spinnerMembers: Spinner
    private lateinit var btnMarkAttendance: com.google.android.material.card.MaterialCardView
    private lateinit var recyclerViewAttendance: RecyclerView
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_mark_attendance)

        dbHelper = DatabaseHelper(this)
        spinnerMembers = findViewById(R.id.spinnerMembers)
        btnMarkAttendance = findViewById(R.id.btnMarkAttendance)
        recyclerViewAttendance = findViewById(R.id.recyclerViewAttendance)

        // Fetch members from the database
        val members = dbHelper.getMembersListAsMap()

        // Create an adapter to populate the Spinner with member names
        val memberNames = members.map { it["name"]!! }
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, memberNames)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerMembers.adapter = adapter

        // Set up the RecyclerView
        recyclerViewAttendance.layoutManager = LinearLayoutManager(this)

        // Set click listener to mark attendance
        btnMarkAttendance.setOnClickListener {
            val selectedMemberName = spinnerMembers.selectedItem as String
            val selectedMember = members.find { it["name"] == selectedMemberName }

            // Add attendance for the selected member
            selectedMember?.let {
                markAttendance(it["id"]!!.toInt())
            }
        }

        // Optionally, display attendance for each member
        displayAttendanceStatus(members)
    }

    private fun markAttendance(memberId: Int) {
        val db = dbHelper.writableDatabase
        val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

        val values = ContentValues().apply {
            put("member_id", memberId)
            put("date", currentDate)
        }

        // Insert attendance into the attendance table
        db.insert("attendance", null, values)

        // Optionally show a toast message to indicate success
        Toast.makeText(this, "Attendance marked successfully", Toast.LENGTH_SHORT).show()

        // Optionally, finish the activity and return to the previous screen
        finish()
    }

    private fun displayAttendanceStatus(members: List<Map<String, String>>) {
        val db = dbHelper.readableDatabase
        val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

        val attendanceList = mutableListOf<Attendance>()

        // Fetch attendance for each member
        for (member in members) {
            val memberId = member["id"]!!.toInt()
            val cursor = db.rawQuery("SELECT * FROM attendance WHERE member_id = ? AND date = ?",
                arrayOf(memberId.toString(), currentDate))

            val status = if (cursor.moveToNext()) "Present" else "Absent"

            // Store the date and status only
            attendanceList.add(Attendance(currentDate, status))  // Show the date here
            cursor.close()
        }

        // Set the adapter for RecyclerView
        recyclerViewAttendance.adapter = AttendanceAdapter(attendanceList)
    }
}
