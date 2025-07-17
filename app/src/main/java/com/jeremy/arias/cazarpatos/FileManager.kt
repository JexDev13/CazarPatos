package com.jeremy.arias.cazarpatos

import android.app.Activity
import java.io.BufferedReader
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStreamReader
import java.io.OutputStreamWriter

class FileManager (private val actividad: Activity) : FileHandler {

    override fun SaveInformation(datosAGrabar: Pair<String, String>) {
        try {
            val outputStream: FileOutputStream = actividad.openFileOutput("duckHunt.txt", Activity.MODE_PRIVATE)
            val writer = OutputStreamWriter(outputStream)
            writer.write("${datosAGrabar.first}\n${datosAGrabar.second}")
            writer.flush()
            writer.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    override fun ReadInformation(): Pair<String, String> {
        var email = ""
        var password = ""
        try {
            val inputStream: FileInputStream = actividad.openFileInput("duckHunt.txt")
            val reader = BufferedReader(InputStreamReader(inputStream))
            email = reader.readLine() ?: ""
            password = reader.readLine() ?: ""
            reader.close()
        } catch (e: FileNotFoundException) {

        } catch (e: IOException) {
            e.printStackTrace()
        }
        return email to password
    }
}