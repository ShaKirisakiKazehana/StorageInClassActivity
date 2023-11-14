package com.example.networkapp
import android.content.Context
import java.io.*

object FileHelper {

    private const val FILE_NAME = "comic_data.txt"

    fun saveComic(context: Context, comicId: String, title: String, description: String, imageUrl: String) {
        val data = "$comicId|$title|$description|$imageUrl"
        try {
            context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE).use {
                it.write(data.toByteArray())
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun loadComic(context: Context): Array<String?> {
        try {
            context.openFileInput(FILE_NAME).use {
                val bytes = ByteArray(it.available())
                it.read(bytes)
                val data = String(bytes)
                return data.split("|").toTypedArray()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return arrayOfNulls(4)
    }
}
