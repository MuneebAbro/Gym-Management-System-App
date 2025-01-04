package com.muneeb.gym.data

data class WorkoutPlan(
    val id: Int,
    val memberId: Int,
    val workoutType: String,
    val duration: Int,
    val intensity: String,
)

