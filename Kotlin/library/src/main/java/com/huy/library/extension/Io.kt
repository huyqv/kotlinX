package com.huy.library.extension

import android.app.Application
import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
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

val filePath: String
    get() = if (Build.VERSION.SDK_INT > -Build.VERSION_CODES.Q) {
        app.getExternalFilesDir(null)?.absolutePath!!
    } else {
        @Suppress("DEPRECATION")
        Environment.getExternalStorageDirectory().toString()
    }

fun readBytesFromAssets(fileName: String): ByteArray? {
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

fun readStringFromAssets(filename: String): String? {
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

fun saveBitmap(name: String, bitmap: Bitmap) {
    val path: String = filePath
    var fOut: OutputStream?
    val file = File(path, "$name.png")
    fOut = FileOutputStream(file)
    bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut)
    fOut.flush()
    fOut.close()
    MediaStore.Images.Media.insertImage(app.contentResolver, file.absolutePath, file.name, file.name)
}

fun newFile(name: String): File {
    return File(filePath, name)
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
        fos = FileOutputStream("$filePath/$fileName")
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
    val dir = File(filePath)
    if (!dir.exists()) {
        dir.mkdirs()
    }
    val file = File("$filePath/$fileName")
    if (file.exists()) return
    copyFile(fileName)

}

fun writeFile(bytes: ByteArray): File? {
    return try {
        val file = File(filePath, "temp.jpg")
        val stream = FileOutputStream(file)
        stream.write(bytes)
        stream.flush()
        stream.close()
        file
    } catch (e: Exception) {
        null
    }
}

fun readFile(fileName: String): String {
    val file = File(filePath, fileName)
    val text = java.lang.StringBuilder()
    try {
        val br = BufferedReader(FileReader(file))
        var line: String?
        while (br.readLine().also { line = it } != null) {
            text.append(line)
            text.append('\n')
        }
        br.close()
    } catch (e: IOException) {
    }
    return text.toString()


}




