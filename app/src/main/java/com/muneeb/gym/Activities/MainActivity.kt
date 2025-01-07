package com.muneeb.gym.Activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.muneeb.gym.databinding.ActivityMainBinding
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.muneeb.gym.Database.DatabaseHelper
import com.muneeb.gym.R
import com.muneeb.gym.adapter.MemberAdapter
import com.muneeb.gym.data.Member
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()

        val dbHelper = DatabaseHelper(this)
        val db = dbHelper.writableDatabase // Opens the database

        dummyDBdata()
        dummyAttendanceData()

        val btnAddMember = findViewById<CardView>(R.id.calCardView)
        btnAddMember.setOnClickListener {
            val intent = Intent(this, AddMember::class.java)
            startActivity(intent)
        }

        val currentDate = SimpleDateFormat("EEE d MMM", Locale.getDefault()).format(Date())
        binding.currentDateTV.text = currentDate

        val totalMembers = dbHelper.getTotalMembers()
        val totalTaskTV = findViewById<TextView>(R.id.totalTaskTV)
        totalTaskTV.text = totalMembers.toString()

        val totalWorkoutPlan = dbHelper.getTotalWorkout()
        val totalWorkoutPlanCount = findViewById<TextView>(R.id.pendingTaskTV)
        totalWorkoutPlanCount.text = totalWorkoutPlan.toString()

        val markAttendanceButton = findViewById<CardView>(R.id.calCardView4)
        markAttendanceButton.setOnClickListener {
            val intent = Intent(this, MarkAttendanceActivity::class.java)
            startActivity(intent)
        }


        val personRemoveCard = findViewById<CardView>(R.id.person_rem)
        personRemoveCard.setOnClickListener {
            val intent = Intent(this, DeleteMemberActivity::class.java)
            startActivity(intent)
        }

        val btnViewWorkoutPlans = findViewById<CardView>(R.id.workoutplancountcard)
        btnViewWorkoutPlans.setOnClickListener {
            val intent = Intent(this, ViewWorkoutPlan::class.java)
            startActivity(intent)
        }


        val btnViewMemberDetails = findViewById<CardView>(R.id.cardView2)
        btnViewMemberDetails.setOnClickListener {
            val intent = Intent(this, MemberDetailsActivity::class.java)
            startActivity(intent)
        }



    }

    private fun dummyAttendanceData() {
        val recyclerViewAttendance = findViewById<RecyclerView>(R.id.recyclerViewAttendance)
        recyclerViewAttendance.layoutManager = LinearLayoutManager(this)

        val dbHelper = DatabaseHelper(this)
        val memberId = 1 // Replace with the ID of the member
        val attendanceList = dbHelper.getAttendanceForMember(memberId)

        val adapter = AttendanceAdapter(attendanceList)
        recyclerViewAttendance.adapter = adapter

    }

    private fun dummyDBdata() {
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewMembers)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val dbHelper = DatabaseHelper(this)
        val membersFromDb = dbHelper.getMembersListAsMap().map {
            Member(
                id = it["id"]!!.toInt(),
                name = it["name"]!!,
                age = it["age"]!!.toInt(),
                email = it["email"]!!,
                phone = it["phone"]!!,
                membershipType = it["membership_type"]!!
            )
        }

        val adapter = MemberAdapter(membersFromDb)
        recyclerView.adapter = adapter
    }


}