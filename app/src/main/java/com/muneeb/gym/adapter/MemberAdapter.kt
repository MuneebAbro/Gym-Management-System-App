package com.muneeb.gym.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.muneeb.gym.R
import com.muneeb.gym.data.Member

class MemberAdapter(private val members: List<Member>) :
    RecyclerView.Adapter<MemberAdapter.MemberViewHolder>() {

    class MemberViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.textViewName)
        val ageTextView: TextView = itemView.findViewById(R.id.textViewAge)
        val membershipTypeTextView: TextView = itemView.findViewById(R.id.textViewMembershipType)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemberViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_member, parent, false)
        return MemberViewHolder(view)
    }

    override fun onBindViewHolder(holder: MemberViewHolder, position: Int) {
        val member = members[position]
        holder.nameTextView.text = member.name
        holder.ageTextView.text = "Age: ${member.age}"
        holder.membershipTypeTextView.text = "Membership: ${member.membershipType}"
    }

    override fun getItemCount(): Int = members.size
}
