package com.muneeb.gym.Database

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.muneeb.gym.data.Attendance
import com.muneeb.gym.data.WorkoutPlan

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_NAME = "gym_management.db"
        const val DATABASE_VERSION = 2
    }

    override fun onCreate(db: SQLiteDatabase) {
        // Create the 'members' table
        val createMembersTable = """
            CREATE TABLE members (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT NOT NULL,
                age INTEGER,
                email TEXT UNIQUE,
                phone TEXT,
                membership_type TEXT
            )
        """
        db.execSQL(createMembersTable)

        // Create the 'payments' table
        val createPaymentsTable = """
            CREATE TABLE payments (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                member_id INTEGER,
                amount REAL,
                payment_date TEXT,
                FOREIGN KEY(member_id) REFERENCES members(id)
            )
        """
        db.execSQL(createPaymentsTable)

        // Create the 'attendance' table
        val createAttendanceTable = """
           CREATE TABLE attendance (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            member_id INTEGER,
            date TEXT,
            FOREIGN KEY(member_id) REFERENCES members(id)
            )
        """
        db.execSQL(createAttendanceTable)

        // Create the 'workout_plans' table
        val createWorkoutPlansTable = """
    CREATE TABLE workout_plans (
        id INTEGER PRIMARY KEY AUTOINCREMENT,
        member_id INTEGER,
        workout_type TEXT,
        duration INTEGER,  
        intensity TEXT,
        FOREIGN KEY(member_id) REFERENCES members(id)
    )
"""
        db.execSQL(createWorkoutPlansTable)

    }


    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        if (oldVersion < 2) {
            // Alter the workout_plans table to add the duration column
            val alterTable = """
                ALTER TABLE workout_plans 
                ADD COLUMN duration INTEGER;
            """
            db.execSQL(alterTable)
        }
    }


    @SuppressLint("Range")
    fun getMembersListAsMap(): List<Map<String, String>> {
        val db = readableDatabase
        val cursor = db.query(
            "members", // Table name
            arrayOf("id", "name", "age", "email", "phone", "membership_type"),
            null,
            null,
            null,
            null,
            null
        )

        val members = mutableListOf<Map<String, String>>()

        while (cursor.moveToNext()) {
            val member = mapOf(
                "id" to cursor.getString(cursor.getColumnIndex("id")),
                "name" to cursor.getString(cursor.getColumnIndex("name")),
                "age" to cursor.getString(cursor.getColumnIndex("age")),
                "email" to cursor.getString(cursor.getColumnIndex("email")),
                "phone" to cursor.getString(cursor.getColumnIndex("phone")),
                "membership_type" to cursor.getString(cursor.getColumnIndex("membership_type"))
            )
            members.add(member)
        }

        cursor.close()
        return members
    }

    // Method to get all attendance records for a member
    // Method to get all attendance records for a member
    @SuppressLint("Range")
    fun getAttendanceForMember(memberId: Int): List<Attendance> {
        val db = readableDatabase
        val cursor = db.query(
            "attendance",
            arrayOf("id", "member_id", "date"),
            "member_id = ?",
            arrayOf(memberId.toString()),
            null,
            null,
            null
        )
        val attendanceList = mutableListOf<Attendance>()

        while (cursor.moveToNext()) {
            val date = cursor.getString(cursor.getColumnIndexOrThrow("date"))

            // Use "Present" or "Absent" based on your attendance logic
            val status = "Present" // Replace with your own logic for determining status

            attendanceList.add(Attendance(date, status))
        }

        cursor.close()
        return attendanceList
    }


    // Helper function to get the member's name from the database
    @SuppressLint("Range")
    private fun getMemberNameById(memberId: Int): String {
        val db = readableDatabase
        val cursor = db.query(
            "members", // Assuming 'members' table has member data
            arrayOf("name"), "id = ?", arrayOf(memberId.toString()), null, null, null
        )

        var memberName = ""
        if (cursor.moveToFirst()) {
            memberName = cursor.getString(cursor.getColumnIndexOrThrow("name"))
        }

        cursor.close()
        return memberName
    }


    fun addWorkoutPlan(workoutPlan: WorkoutPlan): Long {
        val db = writableDatabase
        val values = ContentValues()
        values.put("member_id", workoutPlan.memberId)
        values.put("workout_type", workoutPlan.workoutType)
        values.put("duration", workoutPlan.duration)
        values.put("intensity", workoutPlan.intensity)
        return db.insert("workout_plans", null, values)
    }

    @SuppressLint("Range")
    fun getWorkoutPlansForMember(memberId: Int): List<WorkoutPlan> {
        val db = readableDatabase
        val cursor = db.query(
            "workout_plans", null, "member_id = ?", arrayOf(memberId.toString()), null, null, null
        )

        val workoutPlans = mutableListOf<WorkoutPlan>()
        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndex("id"))
            val workoutType = cursor.getString(cursor.getColumnIndex("workout_type"))
            val duration = cursor.getInt(cursor.getColumnIndex("duration"))
            val intensity = cursor.getString(cursor.getColumnIndex("intensity"))

            workoutPlans.add(WorkoutPlan(id, memberId, workoutType, duration, intensity))
        }
        cursor.close()
        return workoutPlans
    }


    fun getTotalMembers(): Int {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT COUNT(*) FROM members", null)
        var totalMembers = 0
        if (cursor.moveToFirst()) {
            totalMembers = cursor.getInt(0) // Retrieve the count
        }
        cursor.close()
        return totalMembers
    }

    fun getTotalWorkout(): Int {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT COUNT(*) FROM workout_plans", null)
        var totalplans = 0
        if (cursor.moveToFirst()) {
            totalplans = cursor.getInt(0) // Retrieve the count
        }
        cursor.close()
        return totalplans
    }


    fun deleteMember(memberId: Int): Boolean {
        val db = writableDatabase
        return try {
            // First delete related records from other tables
            db.delete("attendance", "member_id = ?", arrayOf(memberId.toString()))
            db.delete("payments", "member_id = ?", arrayOf(memberId.toString()))
            db.delete("workout_plans", "member_id = ?", arrayOf(memberId.toString()))

            // Then delete the member
            val result = db.delete("members", "id = ?", arrayOf(memberId.toString()))
            result > 0
        } catch (e: Exception) {
            false
        }
    }

}