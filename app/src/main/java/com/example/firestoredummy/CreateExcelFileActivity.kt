package com.example.firestoredummy

import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.firestoredummy.databinding.ActivityCreateExcelFileBinding
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import toast
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
        try {
            // Create a new Excel file
            val workbook = XSSFWorkbook()
            val sheet = workbook.createSheet("Sheet1")
            val row = sheet.createRow(0)
            val cell = row.createCell(0)
            cell.setCellValue("Hello")


            val filename = "DemoExcel.xls"
            val downloadsDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            val file = File(downloadsDirectory, filename)
            val fileOutputStream = FileOutputStream(file)
            workbook.write(fileOutputStream)
            fileOutputStream.close()

// Print the path of the Excel file
            val filePath = file.absolutePath
            Log.d("Excel File Path", filePath)
            toast("Excel Created Successfully")
        }
        catch (e:Exception){
            e.printStackTrace()
            toast(e.message.toString())
        }

    }

}
