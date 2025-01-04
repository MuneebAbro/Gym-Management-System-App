package com.muneeb.gym.Activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.muneeb.gym.Database.DatabaseHelper
import com.muneeb.gym.R
import com.muneeb.gym.adapter.WorkoutPlanAdapter

class ViewWorkoutPlan : AppCompatActivity() {
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var recyclerView: RecyclerView
    private lateinit var workoutPlanAdapter: WorkoutPlanAdapter
    private lateinit var btnAssignWorkout: com.google.android.material.card.MaterialCardView
    private var selectedMemberId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_view_workout_plan)

        dbHelper = DatabaseHelper(this)
        recyclerView = findViewById(R.id.recyclerViewWorkoutPlans)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Set up Spinner (dropdown) for selecting member
        val spinner = findViewById<Spinner>(R.id.spinnerMembers)
        val members = dbHelper.getMembersListAsMap()
        val memberNames = members.map { it["name"]!! }

        val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, memberNames)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = spinnerAdapter

        // Set listener for spinner selection
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedMember = members[position]
                selectedMemberId = selectedMember["id"]!!.toInt()
                loadWorkoutPlansForMember(selectedMemberId)
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {
                // Handle case where nothing is selected
            }
        }

        // Set up the button for assigning workouts
        btnAssignWorkout = findViewById(R.id.btnAssignWorkout)
        btnAssignWorkout.setOnClickListener {
            if (selectedMemberId != -1) {
                val intent = Intent(this, AssignWorkoutPlan::class.java)
                intent.putExtra("MEMBER_ID", selectedMemberId)
                startActivity(intent)
            }
        }
    }

    // Load workout plans for the selected member
    private fun loadWorkoutPlansForMember(memberId: Int) {
        val workoutPlans = dbHelper.getWorkoutPlansForMember(memberId)
        workoutPlanAdapter = WorkoutPlanAdapter(workoutPlans)
        recyclerView.adapter = workoutPlanAdapter
    }
}
