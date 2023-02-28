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

        val booksData = dataForExcels()
        binding.creatExcel.setOnClickListener {
            val filePath = createExcelFile(booksData)
            Log.d("Excel File Path", filePath)
        }
    }
    private fun dataForExcels(): List<DataForExcel> {
        val booksData = listOf(
            DataForExcel("English Book", "Morgan", "Straus", "10/20/2000", "EnglishBookURL"),
            DataForExcel("Hindi Book", "Virat", "Sachin", "1/35/2010", "HindiBookURL"),
            DataForExcel("Australia Book", "Warner", "Ponting", "25/20/2002", "AustrilaBookURL"),
        )
        return booksData
    }

    private fun createExcelFile(BooksList: List<DataForExcel>): String {
        // Create a new Excel workbook and sheet
        try {
            val workbook = XSSFWorkbook()
            val sheet = workbook.createSheet("Books Information")

            // Create a header row
            val headerRow = sheet.createRow(0)
            headerRow.createCell(0).setCellValue("Book Name")
            headerRow.createCell(1).setCellValue("Author Name")
            headerRow.createCell(2).setCellValue("Publisher Name")
            headerRow.createCell(3).setCellValue("Date")
            headerRow.createCell(4).setCellValue("Source")

            // Add data rows for each employee
            var rowNum = 1
            for (book in BooksList) {
                val row = sheet.createRow(rowNum++)
                row.createCell(0).setCellValue(book.bookName)
                row.createCell(1).setCellValue(book.authorName)
                row.createCell(2).setCellValue(book.publisherName)
                row.createCell(3).setCellValue(book.date)
                row.createCell(4).setCellValue(book.source)
            }

            // Save the Excel file to external storage
            val filename = "bookDta.xlsx"
            val downloadsDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            val file = File(downloadsDirectory, filename)
            val fileOutputStream = FileOutputStream(file)
            workbook.write(fileOutputStream)
            fileOutputStream.close()
            toast("Excel Created Succesfully")
            // Return the path of the Excel file
            return file.absolutePath
        }
        catch (e:Exception){
            e.printStackTrace()
            toast(e.message.toString())
            return "Error"
        }

    }

}
