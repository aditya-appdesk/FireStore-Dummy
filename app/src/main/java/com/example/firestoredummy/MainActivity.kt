package com.example.firestoredummy

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.firestoredummy.databinding.ActivityMainBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import toast

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
                    toast("Filed can not be empty")

                } else {
                    val title = etHeading.text.toString()
                    val description = etDescription.text.toString()
                    saveDataToFireStore(title,description)
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
     toast("Saved")
        }.addOnFailureListener {
            toast("Error")
        }
    }

}
