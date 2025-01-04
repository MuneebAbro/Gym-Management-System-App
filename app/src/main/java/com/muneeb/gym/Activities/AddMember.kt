package com.muneeb.gym.Activities

import android.content.ContentValues
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.muneeb.gym.Database.DatabaseHelper
import com.muneeb.gym.R

class AddMember : AppCompatActivity() {
    private lateinit var etName: com.google.android.material.textfield.TextInputLayout
    private lateinit var etAge: com.google.android.material.textfield.TextInputLayout
    private lateinit var etEmail: com.google.android.material.textfield.TextInputLayout
    private lateinit var etPhone: com.google.android.material.textfield.TextInputLayout
    private lateinit var etMembershipType: com.google.android.material.textfield.TextInputLayout
    private lateinit var btnSaveMember: com.google.android.material.card.MaterialCardView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_member)

        etName = findViewById(R.id.etName)
        etAge = findViewById(R.id.etAge)
        etEmail = findViewById(R.id.etEmail)
        etPhone = findViewById(R.id.etPhone)
        etMembershipType = findViewById(R.id.etMembershipType)
        btnSaveMember = findViewById(R.id.btnSaveMember)




        btnSaveMember.setOnClickListener {
            // Get the data from the form fields
            val name = etName.editText?.text.toString()
            val age = etAge.editText?.text.toString()
            val email = etEmail.editText?.text.toString()
            val phone = etPhone.editText?.text.toString()
            val membershipType = etMembershipType.editText?.text.toString()

            // Validate input
            if (name.isEmpty() || age.isEmpty() || email.isEmpty() || phone.isEmpty() || membershipType.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            } else {
                // Insert data into database
                insertMember(name, age.toInt(), email, phone, membershipType)
            }
        }
    }

    private fun insertMember(name: String, age: Int, email: String, phone: String, membershipType: String) {
        val dbHelper = DatabaseHelper(this)
        val db = dbHelper.writableDatabase

        // Create a ContentValues object to insert the member data
        val values = ContentValues().apply {
            put("name", name)
            put("age", age)
            put("email", email)
            put("phone", phone)
            put("membership_type", membershipType)
        }

        // Insert the data into the database
        val newRowId = db.insert("members", null, values)

        if (newRowId != -1L) {
            // If insertion was successful, show a success message and finish the activity
            Toast.makeText(this, "Member added successfully", Toast.LENGTH_SHORT).show()
            finish() // Close the activity and return to the previous one
        } else {
            // If insertion failed, show an error message
            Toast.makeText(this, "Error adding member", Toast.LENGTH_SHORT).show()
        }
    }
}