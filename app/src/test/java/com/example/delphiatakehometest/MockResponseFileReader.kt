package com.example.delphiatakehometest

import java.io.FileInputStream
import java.io.InputStreamReader

class MockResponseFileReader (path : String){
    val content: String

    init {
        var overallPath = "src/main/assets/$path"
        val inputStream = FileInputStream(overallPath)

        val reader = InputStreamReader(inputStream)
        content = reader.readText()
        reader.close()
    }
}