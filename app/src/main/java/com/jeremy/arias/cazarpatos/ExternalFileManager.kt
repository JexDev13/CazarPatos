package com.jeremy.arias.cazarpatos

import android.app.Activity
import android.os.Environment
import java.io.*

class FileExternalManager(private val actividad: Activity) : FileHandler {

    private fun isExternalStorageWritable(): Boolean {
        return Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED
    }

    private fun isExternalStorageReadable(): Boolean {
        val state = Environment.getExternalStorageState()
        return state == Environment.MEDIA_MOUNTED || state == Environment.MEDIA_MOUNTED_READ_ONLY
    }

    override fun SaveInformation(datosAGrabar: Pair<String, String>) {
        if (isExternalStorageWritable()) {
            try {
                val archivo = File(actividad.getExternalFilesDir(null), SHAREDINFO_FILENAME)
                BufferedWriter(FileWriter(archivo)).use { writer ->
                    writer.write(datosAGrabar.first)
                    writer.newLine()
                    writer.write(datosAGrabar.second)
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    override fun ReadInformation(): Pair<String, String> {
        if (isExternalStorageReadable()) {
            try {
                val archivo = File(actividad.getExternalFilesDir(null), SHAREDINFO_FILENAME)
                BufferedReader(FileReader(archivo)).use { reader ->
                    val usuario = reader.readLine() ?: ""
                    val contador = reader.readLine() ?: ""
                    return usuario to contador
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return "" to ""
    }
}
