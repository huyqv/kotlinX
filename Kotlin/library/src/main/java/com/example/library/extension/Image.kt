package com.example.library.extension

import android.annotation.TargetApi
import android.app.Application
import android.content.ContentValues
import android.graphics.*
import android.media.Image
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Base64
import androidx.annotation.DrawableRes
import com.example.library.Library
import java.io.*
import java.lang.reflect.InvocationTargetException
import java.net.URL
import java.nio.ByteBuffer
import kotlin.math.min


/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2018/10/28
 * @Description: ...
 * None Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
private val app: Application get() = Library.app

class CompressConfigs(val maxSize: Long, val compressFormat: Bitmap.CompressFormat) {
    val extension: String
        get() {
            return when (compressFormat) {
                Bitmap.CompressFormat.PNG -> ".png"
                Bitmap.CompressFormat.JPEG -> ".jpg"
                else -> ".jpg"
            }
        }
}

/**
 * @param candidate     - Bitmap to check
 * @param targetOptions - Options that have the out* value populated
 * @return true if `candidate` can be used for inBitmap re-use with
 * targetOptions
 */
@TargetApi(Build.VERSION_CODES.KITKAT)
private fun canUseForInBitmap(candidate: Bitmap, targetOptions: BitmapFactory.Options): Boolean {

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
    // On earlier versions, the dimensions must match exactly and the inSampleSize must be 1
        return (candidate.width == targetOptions.outWidth
                && candidate.height == targetOptions.outHeight
                && targetOptions.inSampleSize == 1)


    // From Android 4.4 (KitKat) onward we can re-use if the byte size of the new bitmap
    // is smaller than the reusable bitmap candidate allocation byte count.
    val width = targetOptions.outWidth / targetOptions.inSampleSize
    val height = targetOptions.outHeight / targetOptions.inSampleSize
    val byteCount = width * height * candidate.config.getBytesPerPixel()
    return byteCount <= candidate.allocationByteCount
}

/**
 * Return the byte usage per pixel of a bitmap based on its configuration.
 *
 * @param config The bitmap configuration.
 * @return The byte usage per pixel.
 */
private fun Bitmap.Config.getBytesPerPixel(): Int {
    return when (this) {
        Bitmap.Config.ARGB_8888 -> 4
        Bitmap.Config.RGB_565 -> 2
        @Suppress("DEPRECATION")
        Bitmap.Config.ARGB_4444 -> 2
        Bitmap.Config.ALPHA_8 -> 1
        else -> 1
    }
}

/**
 * Decode and sample down a bitmap from resources to the requested width and height.
 *
 * @param res       The resources object containing the image data
 * @param resId     The resource id of the image data
 * @param width  The requested width of the resulting bitmap
 * @param height The requested height of the resulting bitmap
 * @return A bitmap sampled down from the original with the same aspect ratio and dimensions
 * that are equal to or greater than the requested width and height
 */
fun bitmapFromResources(@DrawableRes resId: Int, width: Int, height: Int): Bitmap {
    // First decode with inJustDecodeBounds=true to check dimensions
    val options = BitmapFactory.Options()
    options.inJustDecodeBounds = true
    BitmapFactory.decodeResource(app.resources, resId, options)

    // Calculate inSampleSize
    options.inSampleSize = calculateInSampleSize(options, width, height)
    // END_INCLUDE (read_bitmap_dimensions)

    // Decode bitmap with inSampleSize set
    options.inJustDecodeBounds = false
    return BitmapFactory.decodeResource(app.resources, resId, options)
}

/**
 * Decode and sample down a bitmap from a file to the requested width and height.
 *
 * @param filename  The full path of the file to decode
 * @param width  The requested width of the resulting bitmap
 * @param height The requested height of the resulting bitmap
 * @return A bitmap sampled down from the original with the same aspect ratio and dimensions
 * that are equal to or greater than the requested width and height
 */
fun bitmapFromFile(filename: String, width: Int, height: Int): Bitmap {
    // First decode with inJustDecodeBounds=true to check dimensions
    val options = BitmapFactory.Options()
    options.inJustDecodeBounds = true
    BitmapFactory.decodeFile(filename, options)

    // Calculate inSampleSize
    options.inSampleSize = calculateInSampleSize(options, width, height)

    // Decode bitmap with inSampleSize set
    options.inJustDecodeBounds = false
    return BitmapFactory.decodeFile(filename, options)
}

/**
 * Decode and sample down a bitmap from a file input stream to the requested width and height.
 *
 * @param fileDescriptor The file descriptor to read from
 * @param width       The requested width of the resulting bitmap
 * @param height      The requested height of the resulting bitmap
 * @return A bitmap sampled down from the original with the same aspect ratio and dimensions
 * that are equal to or greater than the requested width and height
 */
fun bitmapFromFileDescriptor(fileDescriptor: FileDescriptor, width: Int, height: Int): Bitmap {

    // First decode with inJustDecodeBounds=true to check dimensions
    val options = BitmapFactory.Options()
    options.inJustDecodeBounds = true
    BitmapFactory.decodeFileDescriptor(fileDescriptor, null, options)

    // Calculate inSampleSize
    options.inSampleSize = calculateInSampleSize(options, width, height)

    // Decode bitmap with inSampleSize set
    options.inJustDecodeBounds = false

    return BitmapFactory.decodeFileDescriptor(fileDescriptor, null, options)
}

fun bitmapFromInputStream(inputStream: InputStream, width: Int, height: Int): Bitmap? {
    // First decode with inJustDecodeBounds=true to check dimensions
    val options = BitmapFactory.Options()
    options.inJustDecodeBounds = true
    BitmapFactory.decodeStream(inputStream, null, options)

    // Calculate inSampleSize
    options.inSampleSize = calculateInSampleSize(options, width, height)

    // Decode bitmap with inSampleSize set
    options.inJustDecodeBounds = false

    return BitmapFactory.decodeStream(inputStream, null, options)
}

fun bitmapFromUrl(strURL: String, width: Int, height: Int): Bitmap? {
    if (TextUtils.isEmpty(strURL)) {
        return null
    }
    var bitmap: Bitmap? = null
    var inputStream: InputStream? = null
    try {
        // First decode with inJustDecodeBounds=true to check dimensions
        val url = URL(strURL)
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        inputStream = url.openStream()
        BitmapFactory.decodeStream(inputStream, null, options)
        inputStream.safeClose()
        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, width, height)

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false

        inputStream = url.openStream()
        bitmap = BitmapFactory.decodeStream(url.openStream(), null, options)
    } catch (e: IOException) {
        e.printStackTrace()
    } finally {
        inputStream?.safeClose()
    }
    return bitmap
}

/**
 * Calculate an inSampleSize for use in a [BitmapFactory.Options] object when decoding
 * bitmaps using the decode* methods from [BitmapFactory]. This implementation calculates
 * the closest inSampleSize that is a power of 2 and will result in the final decoded bitmap
 * having a width and height equal to or larger than the requested width and height.
 *
 * @param options   An options object with out* params already populated (run through a decode*
 * method with inJustDecodeBounds==true
 * @param width  The requested width of the resulting bitmap
 * @param height The requested height of the resulting bitmap
 * @return The value to be used for inSampleSize
 */
fun calculateInSampleSize(options: BitmapFactory.Options, width: Int, height: Int): Int {
    // BEGIN_INCLUDE (calculate_sample_size)
    // Raw height and width of image
    val h = options.outHeight
    val w = options.outWidth
    var inSampleSize = 1

    if (h > height || w > width) {

        val halfHeight = h / 2
        val halfWidth = w / 2

        // Calculate the largest inSampleSize value that is a power of 2 and keeps both
        // height and width larger than the requested height and width.
        while (halfHeight / inSampleSize > height && halfWidth / inSampleSize > width) {
            inSampleSize *= 2
        }

        // This offers some additional logic in case the image has a strange
        // aspect ratio. For example, a panorama may have a much larger
        // width than height. In these cases the total pixels might still
        // end up being too large to fit comfortably in memory, so we should
        // be more aggressive with sample down the image (=larger inSampleSize).

        var totalPixels = (width * height / inSampleSize).toLong()

        // Anything more than 2x the requested pixels we'll sample down further
        val totalReqPixelsCap = width * height * 2L

        while (totalPixels > totalReqPixelsCap) {
            inSampleSize *= 2
            totalPixels /= 2
        }
    }
    return inSampleSize
    // END_INCLUDE (calculate_sample_size)
}

fun pack(bytes: ByteArray, offset: Int, length: Int, littleEndian: Boolean): Int {
    var fOffset = offset
    var fLength = length
    var step = 1
    if (littleEndian) {
        fOffset += fLength - 1
        step = -1
    }
    var value = 0
    while (fLength-- > 0) {
        value = value shl 8 or (bytes[fOffset].toInt() and 255)
        fOffset += step
    }
    return value
}

fun getBitmap(@DrawableRes res: Int): Bitmap? {
    return BitmapFactory.decodeResource(app.resources, res)
}

@Throws(IOException::class)
fun orientation(src: String): Int {
    var orientation = 1
    try {

        /**
         * if your are targeting only api level >= 5
         * ExifInterface exif = new ExifInterface(src);
         * orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
         */
        if (Build.VERSION.SDK_INT >= 5) {
            val exifClass = Class.forName("android.media.ExifInterface")
            val exifConstructor = exifClass.getConstructor(String::class.java)
            val exifInstance = exifConstructor.newInstance(src)
            val getAttributeInt = exifClass.getMethod("getAttributeInt", String::class.java, Int::class.javaPrimitiveType!!)
            val tagOrientationField = exifClass.getField("TAG_ORIENTATION")
            val tagOrientation = tagOrientationField.get(null) as String
            orientation = getAttributeInt.invoke(exifInstance, tagOrientation, 1) as Int
        }
    } catch (e: ClassNotFoundException) {
        e.printStackTrace()
    } catch (e: SecurityException) {
        e.printStackTrace()
    } catch (e: NoSuchMethodException) {
        e.printStackTrace()
    } catch (e: IllegalArgumentException) {
        e.printStackTrace()
    } catch (e: InstantiationException) {
        e.printStackTrace()
    } catch (e: IllegalAccessException) {
        e.printStackTrace()
    } catch (e: InvocationTargetException) {
        e.printStackTrace()
    } catch (e: NoSuchFieldException) {
        e.printStackTrace()
    }
    return orientation
}

fun orientation(bytes: ByteArray?): Int {
    if (bytes == null) {
        return 0
    }
    var offset = 0
    var length = 0
    // ISO/IEC 10918-1:1993(E)
    while (offset + 3 < bytes.size && bytes[offset++].toInt() and 255 == 255) {
        val marker = bytes[offset].toInt() and 255
        // Check if the marker is a padding.
        if (marker == 255) {
            continue
        }
        offset++
        // Check if the marker is SOI or TEM.
        if (marker == 216 || marker == 1) {
            continue
        }
        // Check if the marker is EOI or SOS.
        if (marker == 217 || marker == 218) {
            break
        }
        // Get the length and check if it is reasonable.
        length = pack(bytes, offset, 2, false)
        if (length < 2 || offset + length > bytes.size) {
            return 0
        }
        // Break if the marker is EXIF in APP1.
        if (marker == 0xE1 && length >= 8 &&
                pack(bytes, offset + 2, 4, false) == 1165519206 &&
                pack(bytes, offset + 6, 2, false) == 0) {
            offset += 8
            length -= 8
            break
        }
        // Skip other markers.
        offset += length
        length = 0
    }
    // JEITA CP-3451 Exif Version 2.2
    if (length > 8) {
        // Identify the byte order.
        var tag = pack(bytes, offset, 4, false)
        if (tag != 1229531648 && tag != 1296891946) {
            return 0
        }
        val littleEndian = tag == 1229531648
        // Get the offset and check if it is reasonable.
        var count = pack(bytes, offset + 4, 4, littleEndian) + 2
        if (count < 10 || count > length) {
            return 0
        }
        offset += count
        length -= count
        // Get the count and go through all the elements.
        count = pack(bytes, offset - 2, 2, littleEndian)
        while (count-- > 0 && length >= 12) {
            // Get the tag and check if it is orientation.
            tag = pack(bytes, offset, 2, littleEndian)
            if (tag == 112) {
                // We do not really care about type and count, do we?
                val orientation = pack(bytes, offset + 8, 2, littleEndian)
                when (orientation) {
                    1 -> return 0
                    3 -> return 180
                    6 -> return 90
                    8 -> return 270
                }
                return 0
            }
            offset += 12
            length -= 12
        }
    }
    return 0
}

fun Image.yuv420toNv21(): ByteArray {
    val crop = this.cropRect
    val format = this.format
    val width = crop.width()
    val height = crop.height()
    val planes = this.planes
    val data = ByteArray(width * height * ImageFormat.getBitsPerPixel(format) / 8)
    val rowData = ByteArray(planes[0].rowStride)

    var channelOffset = 0
    var outputStride = 1
    for (i in planes.indices) {
        when (i) {
            0 -> {
                channelOffset = 0
                outputStride = 1
            }
            1 -> {
                channelOffset = width * height + 1
                outputStride = 2
            }
            2 -> {
                channelOffset = width * height
                outputStride = 2
            }
        }

        val buffer = planes[i].buffer
        val rowStride = planes[i].rowStride
        val pixelStride = planes[i].pixelStride

        val shift = if (i == 0) 0 else 1
        val w = width shr shift
        val h = height shr shift
        buffer.position(rowStride * (crop.top shr shift) + pixelStride * (crop.left shr shift))
        for (row in 0 until h) {
            val length: Int
            if (pixelStride == 1 && outputStride == 1) {
                length = w
                buffer.get(data, channelOffset, length)
                channelOffset += length
            } else {
                length = (w - 1) * pixelStride + 1
                buffer.get(rowData, 0, length)
                for (col in 0 until w) {
                    data[channelOffset] = rowData[col * pixelStride]
                    channelOffset += outputStride
                }
            }
            if (row < h - 1) {
                buffer.position(buffer.position() + rowStride - length)
            }
        }
    }
    return data
}

fun Image.toBytes(): ByteArray? {
    var data: ByteArray? = null
    try {
        if (this.format == ImageFormat.JPEG) {
            val planes = this.planes
            val buffer = planes[0].buffer
            data = ByteArray(buffer.capacity())
            buffer.get(data)
            return data
        } else if (this.format == ImageFormat.YUV_420_888) {
            data = this.yuv420toNv21().nv21toJpeg(this.width, this.height, 100)
        }
    } catch (ex: Exception) {

    }

    return data
}

fun YuvImage.toRgbBitmap(): Bitmap {
    val outStream = ByteArrayOutputStream()
    this.compressToJpeg(Rect(0, 0, this.width, this.height), 100, outStream) // make JPG

    return BitmapFactory.decodeByteArray(outStream.toByteArray(), 0, outStream.size())
}

/**
 * [ByteArray]
 */
fun ByteArray.nv21toJpeg(width: Int, height: Int, quality: Int): ByteArray {
    val out = ByteArrayOutputStream()
    val yuv = YuvImage(this, ImageFormat.NV21, width, height, null)
    yuv.compressToJpeg(Rect(0, 0, width, height), quality, out)
    return out.toByteArray()
}

fun ByteArray.toRawBytes(): ByteArray {
    val rawBitmap = this.toBitmap()
    val stream = ByteArrayOutputStream()
    rawBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
    val rawByteArray = stream.toByteArray()
    rawBitmap.recycle()
    return rawByteArray
}

fun ByteArray.convertImage(pixels: IntArray, exposureCompensation: Double?) {
    if (exposureCompensation != null) {
        for (i in this.indices) {
            val grey = min(((255 and this[i].toInt()) * exposureCompensation).toInt(), 255)
            pixels[i] = -16777216 or 10101 * grey
        }
    } else {
        for (i in this.indices) {
            val grey = 255 and this[i].toInt()
            pixels[i] = -16777216 or 10101 * grey
        }
    }
}

fun ByteArray.toBitmap(config: Bitmap.Config = Bitmap.Config.ALPHA_8, width: Int, height: Int): Bitmap? {
    val bitmap = Bitmap.createBitmap(width, height, config)
    val buffer = ByteBuffer.wrap(this)
    bitmap.copyPixelsFromBuffer(buffer)
    return bitmap
}

fun ByteArray.toBitmap(bitmap: Bitmap, pixels: IntArray, exposureCompensation: Double?): Bitmap {
    val width = bitmap.width
    val height = bitmap.height
    this.convertImage(pixels, exposureCompensation)
    bitmap.setPixels(pixels, 0, width, 0, 0, width, height)
    return bitmap
}

fun ByteArray.toBitmap(width: Int, height: Int, exposureCompensation: Double?): Bitmap {
    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val pixels = IntArray(this.size)

    return this.toBitmap(bitmap, pixels, exposureCompensation)
}

fun ByteArray.toBitmap(width: Int, height: Int): Bitmap {
    var options: BitmapFactory.Options? = null
    if (width > 0 && height > 0) {
        options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeByteArray(this, 0, this.size, options)
        options.inJustDecodeBounds = false
        options.inSampleSize = calculateInSampleSize(options, width, height)
    }
    val srcBitmap = BitmapFactory.decodeByteArray(this, 0, this.size, options)
    val orientation = orientation(this)
    if (srcBitmap != null && orientation != 0) {
        val matrix = Matrix()
        matrix.postRotate(orientation.toFloat())
        val bitmap = Bitmap.createBitmap(srcBitmap, 0, 0, srcBitmap.width, srcBitmap.height, matrix, true)
        srcBitmap.recycle()
        return bitmap
    }
    return srcBitmap
}

/**
 * [Bitmap]
 */
fun Bitmap.size(): Int {
    // From KitKat onward use getAllocationByteCount() as allocated bytes can potentially be
    // larger than bitmap byte count.
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) return this.allocationByteCount
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) return this.byteCount
    return this.rowBytes * this.height
    // Pre HC-MR1
}

fun Bitmap.threshold(threshold: Int = 128): Bitmap {
    val image = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    Canvas(image).drawBitmap(this, 0f, 0f, null)
    for (x in 0 until width) {
        for (y in 0 until height) {
            // get pixel color
            val pixel = this.getPixel(x, y)
            val red = Color.red(pixel)
            val green = Color.green(pixel)
            val blue = Color.blue(pixel)
            var gray = (0.2989 * red + 0.5870 * green + 0.1140 * blue).toInt()
            // use 128 as threshold, above -> white, below -> black
            gray = if (gray > threshold) 255 else 0
            // set new pixel color to output bitmap
            image.setPixel(x, y, Color.argb(255, gray, gray, gray))
        }
    }


    return image
}

fun Bitmap.convert(config: Bitmap.Config): Bitmap {
    val convertedBitmap = Bitmap.createBitmap(width, height, config)
    val canvas = Canvas(convertedBitmap)
    val paint = Paint()
    paint.color = Color.BLACK
    canvas.drawBitmap(this, 0f, 0f, paint)
    return convertedBitmap
}

fun Bitmap.rotate(degrees: Int): Bitmap {
    val matrix = Matrix()
    matrix.postRotate(degrees.toFloat())
    matrix.postScale(-1f, 1f)
    return Bitmap.createBitmap(this, 0, 0, this.width, this.height, matrix, true)
}

fun Bitmap.rotate(src: String): Bitmap {
    return try {
        val orientation = orientation(src)
        if (orientation == 1) {
            return this
        }
        val matrix = Matrix()
        when (orientation) {
            2 -> matrix.setScale(-1f, 1f)
            3 -> matrix.setRotate(180f)
            4 -> {
                matrix.setRotate(180f)
                matrix.postScale(-1f, 1f)
            }
            5 -> {
                matrix.setRotate(90f)
                matrix.postScale(-1f, 1f)
            }
            6 -> matrix.setRotate(90f)
            7 -> {
                matrix.setRotate(-90f)
                matrix.postScale(-1f, 1f)
            }
            8 -> matrix.setRotate(-90f)
            else -> return this
        }
        val oriented = Bitmap.createBitmap(this, 0, 0, this.width, this.height, matrix, true)
        this.recycle()
        oriented
    } catch (e: IOException) {
        e.printStackTrace()
        this
    } catch (e: OutOfMemoryError) {
        e.printStackTrace()
        this
    }

}

fun Bitmap.scale(width: Int, height: Int): Bitmap {
    var w = width
    var h = height
    val originWidth = this.width
    val originHeight = this.height
    val originRatio = 1.0f * originWidth / originHeight
    val desiredRatio = 1.0f * w / h
    var scaleFactor: Float

    // If desire image and origin image have different ratio
    // Origin is width > height and desired is width < height
    if (originRatio > 1.0f && desiredRatio < 1.0f) {
        scaleFactor = 1.0f * w / originWidth
        h = (originHeight * scaleFactor).toInt()
    }

    // Origin is width < height and desired is width > height
    if (originRatio < 1.0f && desiredRatio > 1.0f) {
        scaleFactor = 1.0f * h / originHeight
        w = (originWidth * scaleFactor).toInt()
    }

    // Origin and desired have same type of orientation
    var realWidth = w
    var realHeight = (realWidth / originRatio).toInt()
    if (realHeight > h) {
        realHeight = h
        realWidth = (realHeight * originRatio).toInt()
    }

    return Bitmap.createScaledBitmap(this, realWidth, realHeight, true)
}

fun Bitmap.crop(aspectWidth: Int, aspectHeight: Int): Bitmap {
    val sourceWidth = this.width
    val sourceHeight = this.height

    var width = sourceWidth
    var height = width * aspectHeight / aspectWidth
    var x = 0
    var y = (sourceHeight - height) / 2

    if (height > sourceHeight) {
        height = sourceHeight
        width = height * aspectWidth / aspectHeight
        x = (sourceWidth - width) / 2
        y = 0
    }

    return if (x != 0 || y != 0 || this.width != width || this.height != height) {
        val bmp = Bitmap.createBitmap(this, x, y, width, height)
        bmp.recycle()
        bmp
    } else {
        this
    }

}

fun Bitmap.flipVertical(): Bitmap {
    val matrix = Matrix()
    matrix.preScale(1.0f, -1.0f)
    return Bitmap.createBitmap(this, 0, 0, this.width, this.height, matrix, true)
}

fun Bitmap.flipHorizontal(): Bitmap {
    val matrix = Matrix()
    matrix.preScale(-1.0f, 1.0f)
    return Bitmap.createBitmap(this, 0, 0, this.width, this.height, matrix, true)
}

fun Bitmap.monochrome(): Bitmap {
    val image = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val colorMatrix = ColorMatrix().apply { setSaturation(0f) }
    val paint = Paint().apply { colorFilter = ColorMatrixColorFilter(colorMatrix) }
    Canvas(image).drawBitmap(this, 0f, 0f, paint)
    return image
}

fun Bitmap.roundCorners(radius: Int): Bitmap {

    val output = Bitmap.createBitmap(this.width, this.height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(output)
    val paint = Paint()
    val rect = Rect(0, 0, this.width, this.height)
    val rectF = RectF(rect)
    val roundPx = radius.toFloat()

    paint.isAntiAlias = true
    canvas.drawARGB(0, 0, 0, 0)
    canvas.drawRoundRect(rectF, roundPx, roundPx, paint)

    paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
    canvas.drawBitmap(this, rect, rect, paint)

    this.recycle()
    return output
}

fun Bitmap.save(directory: String?, filename: String, config: CompressConfigs): File? {

    var fDirectory = directory
    var fFilename = filename

    if (fDirectory == null) {
        fDirectory = app.cacheDir.absolutePath
    } else {
        // Check if the given directory exists or try to create it.
        val file = File(fDirectory)
        if (!file.isDirectory && !file.mkdirs()) {
            return null
        }
    }

    val byteCount = this.size().toLong()

    val max = config.maxSize
    var compressRatio = 100
    if (byteCount > max) {
        compressRatio = (100.0f * max / byteCount).toInt()
    }

    var file: File? = null
    var os: OutputStream? = null
    try {
        val format = config.compressFormat
        fFilename += config.extension
        file = File(fDirectory, fFilename)
        os = FileOutputStream(file)
        this.compress(format, compressRatio, os)
    } catch (e: FileNotFoundException) {
        e.printStackTrace()
    } finally {
        os?.safeClose()
    }
    return file
}

fun Bitmap.save(parentDir: File, fileName: String, config: CompressConfigs): File? {
    return this.save(parentDir.absolutePath, fileName, config)
}

fun Bitmap.getImageUri(): Uri? {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
        val bytes = ByteArrayOutputStream()
        this.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        @Suppress("DEPRECATION")
        val path = MediaStore.Images.Media.insertImage(app.contentResolver, this, "Title", null)
        return Uri.parse(path)
    }
    val contentValues = ContentValues().apply {
        put(MediaStore.MediaColumns.DISPLAY_NAME, System.currentTimeMillis().toString())
        put(MediaStore.MediaColumns.MIME_TYPE, "image/*")
        put(MediaStore.MediaColumns.IS_PENDING, 1)
    }
    app.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
    return null
}

/**
 * [String]
 */
fun String?.base64ToBitmap(flag: Int = Base64.DEFAULT): Bitmap? {
    this ?: return null
    return try {
        val decodedBytes = Base64.decode(substring(indexOf(",") + 1), flag)
        BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
    } catch (e: IllegalArgumentException) {
        null
    }
}

fun Closeable.safeClose() {
    try {
        close()
    } catch (ignored: Exception) {
    }
}

/**
 * [android.util.Base64] encode
 */
fun Bitmap.toBytes(): ByteArray {
    val outputStream = ByteArrayOutputStream()
    this.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
    return outputStream.toByteArray()
}

fun ByteArray.encodeToString(flag: Int = Base64.NO_WRAP): String {
    return Base64.encodeToString(this, flag)
}

fun Bitmap.toBase64String(format: Int = Base64.NO_WRAP): String {
    return toBytes().encodeToString(format)
}

/**
 * [android.util.Base64] decode
 */
fun String.decodeToBytes(flag: Int = Base64.NO_WRAP): ByteArray {
    return Base64.decode(this, flag)
}

fun ByteArray.toBitmap(): Bitmap {
    return BitmapFactory.decodeByteArray(this, 0, this.size)
}

fun String.toBitmap(flag: Int = Base64.NO_WRAP): Bitmap {
    return decodeToBytes(flag).toBitmap()
}