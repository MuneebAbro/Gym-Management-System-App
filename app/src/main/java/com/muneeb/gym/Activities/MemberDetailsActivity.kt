package com.muneeb.gym.Activities

import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.muneeb.gym.Database.DatabaseHelper
import com.muneeb.gym.R
import com.muneeb.gym.adapter.WorkoutPlanAdapter

class MemberDetailsActivity : AppCompatActivity() {
    private lateinit var spinnerMembers: Spinner
    private lateinit var tvMemberDetails: TextView
    private lateinit var recyclerViewAttendance: RecyclerView
    private lateinit var recyclerViewWorkoutPlans: RecyclerView
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_member_details)

        spinnerMembers = findViewById(R.id.spinnerMembers)
        recyclerViewAttendance = findViewById(R.id.recyclerViewAttendance)
        recyclerViewWorkoutPlans = findViewById(R.id.recyclerViewWorkoutPlans)
        dbHelper = DatabaseHelper(this)

        // Fetch members from the database
        val members = dbHelper.getMembersListAsMap()

        // Create an adapter to populate the Spinner
        val memberNames = members.map { it["name"]!! }
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, memberNames)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerMembers.adapter = adapter

        // Set a listener for the spinner to show selected member's details
        spinnerMembers.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedMemberName = parentView?.getItemAtPosition(position) as String
                val selectedMember = members.find { it["name"] == selectedMemberName }

                // Show the details of the selected member
                selectedMember?.let {
                    findViewById<TextView>(R.id.tvName).text = it["name"]
                    findViewById<TextView>(R.id.tvAge).text = it["age"]
                    findViewById<TextView>(R.id.tvEmail).text = it["email"]
                    findViewById<TextView>(R.id.tvPhone).text = it["phone"]
                    findViewById<TextView>(R.id.tvMembership).text = it["membership_type"]

                    // Load and display the member's attendance and workout plans
                    val memberId = it["id"]!!.toInt()
                    loadAttendance(memberId)
                    loadWorkoutPlans(memberId)
                }
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {
                // No action needed
            }
        })
    }

    // Function to load attendance data
    private fun loadAttendance(memberId: Int) {
        val attendanceList = dbHelper.getAttendanceForMember(memberId)

        val attendanceAdapter = AttendanceAdapter(attendanceList)
        recyclerViewAttendance.layoutManager = LinearLayoutManager(this)
        recyclerViewAttendance.adapter = attendanceAdapter
    }

    // Function to load workout plans data
    private fun loadWorkoutPlans(memberId: Int) {
        val workoutPlansList = dbHelper.getWorkoutPlansForMember(memberId)

        val workoutPlansAdapter = WorkoutPlanAdapter(workoutPlansList)
        recyclerViewWorkoutPlans.layoutManager = LinearLayoutManager(this)
        recyclerViewWorkoutPlans.adapter = workoutPlansAdapter
    }
}