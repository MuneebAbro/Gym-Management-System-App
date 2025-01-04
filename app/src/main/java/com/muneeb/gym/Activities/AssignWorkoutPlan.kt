package com.muneeb.gym.Activities

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.muneeb.gym.Database.DatabaseHelper
import com.muneeb.gym.R
import com.muneeb.gym.data.WorkoutPlan
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AssignWorkoutPlan : AppCompatActivity() {
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var etWorkoutType: EditText
    private lateinit var etDuration: EditText
    private lateinit var etIntensity: EditText
    private lateinit var btnSaveWorkout: com.google.android.material.card.MaterialCardView
    private var memberId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_assign_workout_plan)
        dbHelper = DatabaseHelper(this)

        memberId = intent.getIntExtra("MEMBER_ID", -1)

        // Bind views
        etWorkoutType = findViewById(R.id.etWorkoutType)
        etDuration = findViewById(R.id.etDuration)
        etIntensity = findViewById(R.id.etIntensity)
        btnSaveWorkout = findViewById(R.id.btnSaveWorkout)

        btnSaveWorkout.setOnClickListener {
            val workoutType = etWorkoutType.text.toString()
            val durationString = etDuration.text.toString()
            val intensity = etIntensity.text.toString()

            // Validate input
            if (workoutType.isNotBlank() && durationString.isNotBlank() && intensity.isNotBlank()) {
                val duration = durationString.toIntOrNull()
                if (duration != null) {

                    // Add the workout plan to the database
                    val workoutPlan = WorkoutPlan(
                        id = 0, // Auto-generated ID
                        memberId = memberId,
                        workoutType = workoutType,
                        duration = duration,
                        intensity = intensity,
                    )

                    val success = dbHelper.addWorkoutPlan(workoutPlan)

                    if (success > 0) {
                        Toast.makeText(this, "Workout plan assigned successfully!", Toast.LENGTH_SHORT).show()
                        finish() // Close the activity
                    } else {
                        Toast.makeText(this, "Failed to assign workout plan.", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "Please enter a valid duration.", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Please fill all the fields.", Toast.LENGTH_SHORT).show()
            }
        }
    }

}
