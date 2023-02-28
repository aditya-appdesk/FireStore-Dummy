package com.example.firestoredummy

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.firestoredummy.databinding.ActivityCreateExcelFileBinding
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.File
import java.io.FileOutputStream

class CreateExcelFileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreateExcelFileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateExcelFileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.creatExcel.setOnClickListener {
            createAndSave()
        }

    }

    fun createAndSave() {
        // Create a new Excel file
        val workbook = XSSFWorkbook()
        val sheet = workbook.createSheet("Sheet1")
        val row = sheet.createRow(0)
        val cell = row.createCell(0)
        cell.setCellValue("Hello")

// Save the Excel file to external storage
        val filename = "myExcel.xlsx"
        val file = File(getExternalFilesDir(null), filename)
        val fileOutputStream = FileOutputStream(file)
        workbook.write(fileOutputStream)
        fileOutputStream.close()

// Print the path of the Excel file
        val filePath = file.absolutePath
        Log.d("Excel File Path", filePath)
    }

}
