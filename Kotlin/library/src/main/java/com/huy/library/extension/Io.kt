package com.huy.library.extension

import android.app.Application
import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import com.huy.library.Library
import java.io.*

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2019/09/09
 * @Description: ...
 * None Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
private val app: Application get() = Library.app

val path: String get() = app.getExternalFilesDir(null)?.absolutePath!!

fun readBytes(fileName: String): ByteArray? {
    return try {
        val inputStream = app.assets.open(fileName)
        val bytes = ByteArray(inputStream.available())
        inputStream.read(bytes)
        inputStream.close()
        return bytes
    } catch (e: FileNotFoundException) {
        null
    }

}

fun readString(filename: String): String? {
    return try {
        val sb = StringBuilder()
        BufferedReader(InputStreamReader(app.assets.open(filename))).useLines { lines ->
            lines.forEach {
                sb.append(it)
            }
        }
        return sb.toString()
    } catch (e: FileNotFoundException) {
        null
    }
}

fun ByteArray.writeFile(): File? {
    return try {
        val file = File(path, "temp.jpg")
        val stream = FileOutputStream(file)
        stream.write(this)
        stream.flush()
        stream.close()
        file
    } catch (e: Exception) {
        null
    }
}

fun File.getUri(): Uri? {

    val filePath = this.absolutePath

    val cursor = app.contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            arrayOf(MediaStore.Images.Media._ID),
            null,
            arrayOf(filePath), null)

    if (cursor != null && cursor.moveToFirst()) {
        val id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID))
        cursor.close()
        return Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "" + id)
    }

    if (this.exists()) {
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, System.currentTimeMillis().toString())
            put(MediaStore.MediaColumns.MIME_TYPE, "image/*")
            put(MediaStore.MediaColumns.IS_PENDING, 1)
        }
        return app.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
    }

    return null

}

fun downloadFile(name: String): File {
    return File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).absoluteFile, name)
}

fun open(file: File) {
    try {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive")
        Library.app.startActivity(Intent.createChooser(intent, ""))
    } catch (e: Exception) {
    }
}

fun copyFile(fileName: String) {
    var inputStream: InputStream? = null
    var fos: FileOutputStream? = null
    try {
        inputStream = app.assets.open(fileName)
        fos = FileOutputStream("$path/$fileName")
        val buffer = ByteArray(1024)
        var read: Int = inputStream.read(buffer)
        while (read >= 0) {
            fos.write(buffer, 0, read)
            read = inputStream.read(buffer)
        }
    } catch (e: IOException) {
    }
    inputStream?.close()
    fos?.flush()
    fos?.close()
}

fun createFile(fileName: String) {
    val dir = File(path)
    if (!dir.exists()) {
        dir.mkdirs()
    }
    val file = File("$path/$fileName")
    if (file.exists()) return
    copyFile(fileName)

}





