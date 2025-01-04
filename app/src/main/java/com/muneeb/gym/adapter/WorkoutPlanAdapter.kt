package com.muneeb.gym.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.muneeb.gym.R
import com.muneeb.gym.data.WorkoutPlan

class WorkoutPlanAdapter(private val workoutPlans: List<WorkoutPlan>) :
    RecyclerView.Adapter<WorkoutPlanAdapter.WorkoutPlanViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkoutPlanViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_workout_plan, parent, false)
        return WorkoutPlanViewHolder(view)
    }

    override fun onBindViewHolder(holder: WorkoutPlanViewHolder, position: Int) {
        val workoutPlan = workoutPlans[position]
        holder.bind(workoutPlan)
    }

    override fun getItemCount(): Int {
        return workoutPlans.size
    }

    class WorkoutPlanViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textViewWorkoutType: TextView = itemView.findViewById(R.id.textViewWorkoutType)
        private val textViewDuration: TextView = itemView.findViewById(R.id.textViewDuration)
        private val textViewIntensity: TextView = itemView.findViewById(R.id.textViewIntensity)

        fun bind(workoutPlan: WorkoutPlan) {
            textViewWorkoutType.text = "Workout: ${workoutPlan.workoutType}"
            textViewDuration.text = "Duration: ${workoutPlan.duration} minutes"
            textViewIntensity.text = "Intensity: ${workoutPlan.intensity}"
        }
    }
}
