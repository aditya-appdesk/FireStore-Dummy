package com.example.firestoredummy

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.firestoredummy.databinding.ActivityMainBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import toast
import java.io.InputStream

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat



class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val db = Firebase.firestore
    private val STORAGE_PERMISSION_CODE = 1

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
                    saveDataToFireStore(title, description)
                }
            }
        }

        val data = readExcelFromRaw(this, R.raw.boooook)
        Log.i("Data", data.toString())
        binding.apply {
            uploadButton.setOnClickListener {
                checkStoragePermission(this@MainActivity)
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

    private fun readExcelFromRaw(context: Context, resourceId: Int): List<DataForExcel> {
        val inputStream: InputStream = context.resources.openRawResource(resourceId)
        val workbook = HSSFWorkbook(inputStream)
        val sheet = workbook.getSheetAt(0)
        val listOfResults = mutableListOf<DataForExcel>()
        sheet.forEachIndexed { indexr, row ->
            val data = mutableListOf<String>()
            row.forEach {
                if (it.toString().isNotBlank())
                    data.add(it.toString())
            }
            val res = DataForExcel(data.get(0), data.get(1), data.get(2), data.get(3), data.get(4))
            listOfResults.add(res)
        }
        Log.d("Answer", listOfResults.groupBy { it.bookName }.toString())
        return listOfResults
    }

    private fun checkStoragePermission(activity: Activity) {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED
        ) {
            // Permission is not granted, request for permission
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                STORAGE_PERMISSION_CODE
            )
        } else {
            // Permission is granted, continue with your task
            // ...
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted, continue with your task
                // ...
            } else {
                // Permission is not granted, show a message or handle the failure
                // ...
            }
    }


}}

