package com.example.firestoredummy

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.firestoredummy.databinding.ActivityMainBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import toast

import android.content.Context
import android.util.Log
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import java.io.InputStream



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

       val data = readExcelFromRaw(this,R.raw.boooook)
        Log.i("Data",data.toString())
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

     fun readExcelFromRaw(context: Context, resourceId: Int): List<List<String>> {
        val inputStream: InputStream = context.resources.openRawResource(resourceId)
        val workbook = HSSFWorkbook(inputStream)
        val sheet = workbook.getSheetAt(0)
        val rows = sheet.iterator()

        val result = mutableListOf<List<String>>()

        while (rows.hasNext()) {
            val currentRow = rows.next()
            val cells = currentRow.iterator()

            val rowValues = mutableListOf<String>()
            while (cells.hasNext()) {
                val cell = cells.next()
                if(cell.toString().isNotBlank()){
                    rowValues.add(cell.toString())
                }
            }

            result.add(rowValues)
        }
        return result
    }
}
