package com.example.firestoredummy

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.firestoredummy.databinding.ActivityMainBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import toast
import java.io.InputStream


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val db = Firebase.firestore
    private val STORAGE_PERMISSION_CODE = 1
    private val PICKFILE_REQUEST_CODE = 11
    lateinit var uri: Uri

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

        binding.apply {
            uploadButton.setOnClickListener {
                if (checkStoragePermission(this@MainActivity)) {
                    showFilePicker(this@MainActivity)
                }
            }
        }
        binding.button.setOnClickListener {
            startActivity(Intent(this@MainActivity, CreateExcelFileActivity::class.java))
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

    private fun readExcelFromRaw(context: Context, fileUri: Uri): List<DataForExcel> {
        val inputStream: InputStream? = context.contentResolver.openInputStream(fileUri)
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

    private fun checkStoragePermission(activity: Activity): Boolean {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED
        ) {
            // Permission is not granted, request for permission

            ActivityCompat.requestPermissions(
                activity,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                STORAGE_PERMISSION_CODE
            )
            return false
        } else {
            // Permission is granted, continue with your task
            // ...
            return true
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
    }

    private fun handleFilePickerResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?,
        onFileSelected: (Uri) -> Unit
    ) {
        if (requestCode == PICKFILE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val uri: Uri? = data?.data
            if (uri != null) {
                onFileSelected(uri)
                readExcelFromRaw(this@MainActivity, uri)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        handleFilePickerResult(requestCode, resultCode, data) { fileUri ->
            // Do something with the selected file URI
            uri = fileUri
            Log.d("FilePicker", fileUri.toString())
        }
    }

    fun showFilePicker(activity: Activity) {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "*/*"
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        val chooserIntent = Intent.createChooser(intent, "Select File")
        activity.startActivityForResult(chooserIntent, PICKFILE_REQUEST_CODE)
    }


}

