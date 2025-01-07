package com.muneeb.gym.Activities

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.muneeb.gym.Database.DatabaseHelper
import com.muneeb.gym.R

class DeleteMemberActivity : AppCompatActivity() {
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var memberSpinner: Spinner
    private lateinit var deleteButton: com.google.android.material.card.MaterialCardView
    private var members: List<Map<String, String>> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_delete_member)

        dbHelper = DatabaseHelper(this)
        memberSpinner = findViewById(R.id.memberSpinner)
        deleteButton = findViewById(R.id.deleteBtn)

        loadMembers()
        setupSpinner()
        setupDeleteButton()
    }

    private fun loadMembers() {
        members = dbHelper.getMembersListAsMap()
    }

    private fun setupSpinner() {
        val memberNames = members.map { "${it["name"]} (ID: ${it["id"]})" }
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, memberNames)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        memberSpinner.adapter = adapter
    }

    private fun setupDeleteButton() {
        deleteButton.setOnClickListener {
            if (members.isEmpty()) {
                Toast.makeText(this, "No members to delete", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val selectedPosition = memberSpinner.selectedItemPosition
            val selectedMember = members[selectedPosition]
            val memberId = selectedMember["id"]?.toInt() ?: return@setOnClickListener

            showConfirmationDialog(memberId, selectedMember["name"] ?: "Unknown")
        }
    }

    private fun showConfirmationDialog(memberId: Int, memberName: String) {
        AlertDialog.Builder(this)
            .setTitle("Delete Member")
            .setMessage("Are you sure you want to delete $memberName?")
            .setPositiveButton("Yes") { _, _ ->
                deleteMember(memberId)
            }
            .setNegativeButton("No", null)
            .show()
    }

    private fun deleteMember(memberId: Int) {
        val success = dbHelper.deleteMember(memberId)
        if (success) {
            Toast.makeText(this, "Member deleted successfully", Toast.LENGTH_SHORT).show()
            loadMembers()
            setupSpinner()
        } else {
            Toast.makeText(this, "Failed to delete member", Toast.LENGTH_SHORT).show()
        }
    }
}