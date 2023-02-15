package com.example.firestoredummy

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.firestoredummy.databinding.ActivityMainBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.button.setOnClickListener {
            binding.apply {
                if (etHeading.text.isEmpty() || etDescription.text.isEmpty()) {
                    Toast.makeText(this@MainActivity, "Filed can not be empty", Toast.LENGTH_SHORT)
                        .show()

                } else {
                    val title = etHeading.text.toString()
                    val description = etDescription.text.toString()
                    saveDataToFireStore(title,description)
                    //          Toast.makeText(this@MainActivity, "Good to Go", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun saveDataToFireStore(title: String, description: String) {
        val notes = hashMapOf(
            "Title" to title,
            "Description" to description
        )
        db.collection("Notes").add(notes).addOnSuccessListener {
            Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
        }
    }

}
