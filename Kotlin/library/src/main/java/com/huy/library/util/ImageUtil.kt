package com.huy.library.util

import android.annotation.TargetApi
import android.content.res.Resources
import android.graphics.*
import android.media.Image
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Base64
import android.util.Log
import android.view.View
import androidx.annotation.DrawableRes
import com.huy.library.Library
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
object ImageUtil {

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
        val byteCount = width * height * getBytesPerPixel(candidate.config)
        return byteCount <= candidate.allocationByteCount
    }

    /**
     * Return the byte usage per pixel of a bitmap based on its configuration.
     *
     * @param config The bitmap configuration.
     * @return The byte usage per pixel.
     */
    private fun getBytesPerPixel(config: Bitmap.Config): Int {
        if (config == Bitmap.Config.ARGB_8888) {
            return 4
        } else if (config == Bitmap.Config.RGB_565) {
            return 2
        } else if (config == Bitmap.Config.ARGB_4444) {
            return 2
        } else if (config == Bitmap.Config.ALPHA_8) {
            return 1
        }
        return 1
    }

    /**
     * Decode and sample down a bitmap from resources to the requested width and height.
     *
     * @param res       The resources object containing the image data
     * @param resId     The resource id of the image data
     * @param reqWidth  The requested width of the resulting bitmap
     * @param reqHeight The requested height of the resulting bitmap
     * @return A bitmap sampled down from the original with the same aspect ratio and dimensions
     * that are equal to or greater than the requested width and height
     */
    fun decodeSampledBitmapFromResource(res: Resources, resId: Int, reqWidth: Int, reqHeight: Int): Bitmap {
        // First decode with inJustDecodeBounds=true to check dimensions
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeResource(res, resId, options)

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight)
        // END_INCLUDE (read_bitmap_dimensions)

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false
        return BitmapFactory.decodeResource(res, resId, options)
    }

    /**
     * Decode sample bitmap and resize to required width, height in that its aspect's ratio is
     * maintained.
     *
     * @param res
     * @param resId
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    fun decodeSampledBitmapFromResourceWithResize(res: Resources, resId: Int, reqWidth: Int, reqHeight: Int): Bitmap {
        val bitmap = decodeSampledBitmapFromResource(res, resId, reqWidth, reqHeight)

        return scaleBitmap(bitmap, reqWidth, reqHeight)
    }

    /**
     * Scale bitmap to the required width, height. The scaled bitmap will maintain its original
     * aspect's ratio.
     *
     * @param origin    Original bitmap.
     * @param requestWidth  Required currentScale width.
     * @param requestHeight Required currentScale height.
     * @return Scale bitmap that maintains original aspect's ratio.
     */
    private fun scaleBitmap(origin: Bitmap, requestWidth: Int, requestHeight: Int): Bitmap {
        var reqWidth = requestWidth
        var reqHeight = requestHeight
        val originWidth = origin.width
        val originHeight = origin.height
        val originRatio = 1.0f * originWidth / originHeight
        val desiredRatio = 1.0f * reqWidth / reqHeight
        var scaleFactor: Float

        // If desire image and origin image have different ratio
        // Origin is width > height and desired is width < height
        if (originRatio > 1.0f && desiredRatio < 1.0f) {
            scaleFactor = 1.0f * reqWidth / originWidth
            reqHeight = (originHeight * scaleFactor).toInt()
        }

        // Origin is width < height and desired is width > height
        if (originRatio < 1.0f && desiredRatio > 1.0f) {
            scaleFactor = 1.0f * reqHeight / originHeight
            reqWidth = (originWidth * scaleFactor).toInt()
        }

        // Origin and desired have same type of orientation
        var realWidth = reqWidth
        var realHeight = (realWidth / originRatio).toInt()
        if (realHeight > reqHeight) {
            realHeight = reqHeight
            realWidth = (realHeight * originRatio).toInt()
        }

        return Bitmap.createScaledBitmap(origin, realWidth, realHeight, true)
    }

    /**
     * Decode bitmap from resource then currentScale to required size. Scale image will maintain original
     * aspect's ratio.
     *
     * @param res       Android resource
     * @param resId     Resource id of image
     * @param reqWidth  Required width
     * @param reqHeight Required height
     * @return A bitmap from resource id with required size
     */
    fun decodeBitmapWithSize(res: Resources, resId: Int, reqWidth: Int, reqHeight: Int): Bitmap {
        val bitmap = BitmapFactory.decodeResource(res, resId)
        return scaleBitmap(bitmap, reqWidth, reqHeight)
    }

    /**
     * Decode and sample down a bitmap from a file to the requested width and height.
     *
     * @param filename  The full path of the file to decode
     * @param reqWidth  The requested width of the resulting bitmap
     * @param reqHeight The requested height of the resulting bitmap
     * @return A bitmap sampled down from the original with the same aspect ratio and dimensions
     * that are equal to or greater than the requested width and height
     */
    fun decodeSampledBitmapFromFile(filename: String, reqWidth: Int, reqHeight: Int): Bitmap {
        // First decode with inJustDecodeBounds=true to check dimensions
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeFile(filename, options)

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight)

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false
        return BitmapFactory.decodeFile(filename, options)
    }

    /**
     * Decode and sample down a bitmap from a file input stream to the requested width and height.
     *
     * @param fileDescriptor The file descriptor to read from
     * @param reqWidth       The requested width of the resulting bitmap
     * @param reqHeight      The requested height of the resulting bitmap
     * @return A bitmap sampled down from the original with the same aspect ratio and dimensions
     * that are equal to or greater than the requested width and height
     */
    fun decodeSampledBitmapFromDescriptor(fileDescriptor: FileDescriptor, reqWidth: Int, reqHeight: Int): Bitmap {

        // First decode with inJustDecodeBounds=true to check dimensions
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeFileDescriptor(fileDescriptor, null, options)

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight)

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false

        return BitmapFactory.decodeFileDescriptor(fileDescriptor, null, options)
    }

    fun decodeSampleBitmapFromStream(inputStream: InputStream, reqWidth: Int, reqHeight: Int): Bitmap? {
        // First decode with inJustDecodeBounds=true to check dimensions
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeStream(inputStream, null, options)

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight)

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false

        return BitmapFactory.decodeStream(inputStream, null, options)
    }

    fun decodeSampleBitmapFromUrl(strURL: String, reqWidth: Int, reqHeight: Int): Bitmap? {
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
            safeClose(inputStream)

            // Calculate inSampleSize
            options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight)

            // Decode bitmap with inSampleSize set
            options.inJustDecodeBounds = false

            inputStream = url.openStream()
            bitmap = BitmapFactory.decodeStream(url.openStream(), null, options)
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            safeClose(inputStream)
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
     * @param reqWidth  The requested width of the resulting bitmap
     * @param reqHeight The requested height of the resulting bitmap
     * @return The value to be used for inSampleSize
     */
    fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
        // BEGIN_INCLUDE (calculate_sample_size)
        // Raw height and width of image
        val height = options.outHeight
        val width = options.outWidth
        var inSampleSize = 1

        if (height > reqHeight || width > reqWidth) {

            val halfHeight = height / 2
            val halfWidth = width / 2

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while (halfHeight / inSampleSize > reqHeight && halfWidth / inSampleSize > reqWidth) {
                inSampleSize *= 2
            }

            // This offers some additional logic in case the image has a strange
            // aspect ratio. For example, a panorama may have a much larger
            // width than height. In these cases the total pixels might still
            // end up being too large to fit comfortably in memory, so we should
            // be more aggressive with sample down the image (=larger inSampleSize).

            var totalPixels = (width * height / inSampleSize).toLong()

            // Anything more than 2x the requested pixels we'll sample down further
            val totalReqPixelsCap = (reqWidth * reqHeight * 2).toLong()

            while (totalPixels > totalReqPixelsCap) {
                inSampleSize *= 2
                totalPixels /= 2
            }
        }
        return inSampleSize
        // END_INCLUDE (calculate_sample_size)
    }

    /**
     * Get the size in bytes of a bitmap in a Bitmap. Note that from Android 4.4 (KitKat)
     * onward this returns the allocated memory size of the bitmap which can be larger than the
     * actual bitmap data byte count (in the case it was re-used).
     *
     * @param bitmap
     * @return size in bytes
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    fun size(bitmap: Bitmap): Int {
        // From KitKat onward use getAllocationByteCount() as allocated bytes can potentially be
        // larger than bitmap byte count.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) return bitmap.allocationByteCount
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) return bitmap.byteCount
        return bitmap.rowBytes * bitmap.height
        // Pre HC-MR1
    }

    fun getRoundedCorner(bitmap: Bitmap, radius: Int): Bitmap {

        val output = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(output)
        val paint = Paint()
        val rect = Rect(0, 0, bitmap.width, bitmap.height)
        val rectF = RectF(rect)
        val roundPx = radius.toFloat()

        paint.isAntiAlias = true
        canvas.drawARGB(0, 0, 0, 0)
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint)

        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        canvas.drawBitmap(bitmap, rect, rect, paint)

        bitmap.recycle()
        return output
    }


    /**
     * IO
     */
    fun save(bitmap: Bitmap, directory: String?, filename: String, config: CompressConfigs): File? {

        var fDirectory = directory
        var fFilename = filename

        if (fDirectory == null) {
            fDirectory = Library.app.cacheDir.absolutePath
        } else {
            // Check if the given directory exists or try to create it.
            val file = File(fDirectory)
            if (!file.isDirectory && !file.mkdirs()) {
                return null
            }
        }

        val byteCount = size(bitmap).toLong()

        val max = config.maxSize
        var compressRatio = 100
        if (byteCount > max) {
            compressRatio = (100.0f * max / byteCount).toInt()
        }

        var file: File? = null
        var os: OutputStream? = null
        try {
            val format = config.compressFormat
            fFilename = fFilename + config.extension
            file = File(fDirectory, fFilename)
            os = FileOutputStream(file)
            bitmap.compress(format, compressRatio, os)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } finally {
            safeClose(os)
        }
        return file
    }

    fun save(bitmap: Bitmap, parentDir: File, fileName: String, config: CompressConfigs): File? {
        return save(bitmap, parentDir.absolutePath, fileName, config)
    }

    fun uri(format: Bitmap.CompressFormat, quality: Int, bitmap: Bitmap?): Uri? {
        if (bitmap == null || bitmap.isRecycled) {
            return null
        }
        try {
            val outputStream = ByteArrayOutputStream()
            bitmap.compress(format, quality, outputStream)

            val path = MediaStore.Images.Media.insertImage(Library.app.contentResolver, bitmap, "", null)
            safeClose(outputStream)
            return Uri.parse(path)
        } catch (e: Exception) {
        }

        return null
    }

    fun safeClose(closeable: Closeable?) {
        closeable ?: return
        try {
            closeable.close()
        } catch (ignored: Exception) {
        }
    }

    fun convert(raw: ByteArray, pixels: IntArray, exposureCompensation: Double?) {
        if (exposureCompensation != null) {

            for (i in raw.indices) {
                val grey = min(((255 and raw[i].toInt()) * exposureCompensation).toInt(), 255)
                pixels[i] = -16777216 or 10101 * grey
            }
        } else {
            for (i in raw.indices) {
                val grey = 255 and raw[i].toInt()
                pixels[i] = -16777216 or 10101 * grey
            }
        }
    }

    fun formBytes(bitmap: Bitmap, pixels: IntArray, raw: ByteArray, exposureCompensation: Double?): Bitmap {
        val width = bitmap.width
        val height = bitmap.height

        convert(raw, pixels, exposureCompensation)

        bitmap.setPixels(pixels, 0, width, 0, 0, width, height)

        return bitmap
    }

    fun formBytes(width: Int, height: Int, raw: ByteArray, exposureCompensation: Double?): Bitmap {
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val pixels = IntArray(raw.size)

        return formBytes(bitmap, pixels, raw, exposureCompensation)
    }

    fun getBGR(yuvImage: YuvImage): Bitmap {
        val outStream = ByteArrayOutputStream()
        yuvImage.compressToJpeg(Rect(0, 0, yuvImage.width, yuvImage.height), 100, outStream) // make JPG

        return BitmapFactory.decodeByteArray(outStream.toByteArray(), 0, outStream.size())
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


    /**
     * Convert
     */
    fun nv21toJpeg(nv21: ByteArray, width: Int, height: Int, quality: Int): ByteArray {
        val out = ByteArrayOutputStream()
        val yuv = YuvImage(nv21, ImageFormat.NV21, width, height, null)
        yuv.compressToJpeg(Rect(0, 0, width, height), quality, out)
        return out.toByteArray()
    }

    fun yuv420toNv21(image: Image): ByteArray {
        val crop = image.cropRect
        val format = image.format
        val width = crop.width()
        val height = crop.height()
        val planes = image.planes
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


    /**
     * Editing
     */
    fun rotate(bitmap: Bitmap, degrees: Int): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(degrees.toFloat())
        matrix.postScale(-1f, 1f)

        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }

    fun rotate(bitmap: Bitmap, src: String): Bitmap {
        try {
            val orientation = orientation(src)

            if (orientation == 1) {
                return bitmap
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
                else -> return bitmap
            }

            try {
                val oriented = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
                bitmap.recycle()
                return oriented
            } catch (e: OutOfMemoryError) {
                e.printStackTrace()
                return bitmap
            }

        } catch (e: IOException) {
            e.printStackTrace()
        }

        return bitmap
    }

    fun crop(bitmap: Bitmap, aspectWidth: Int, aspectHeight: Int): Bitmap {
        val sourceWidth = bitmap.width
        val sourceHeight = bitmap.height

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

        return if (x != 0 || y != 0 || bitmap.width != width || bitmap.height != height) {
            val bmp = Bitmap.createBitmap(bitmap, x, y, width, height)
            bmp.recycle()
            bmp
        } else {
            bitmap
        }

    }

    fun crop(bytes: ByteArray, aspectWidth: Int, aspectHeight: Int): Bitmap {
        return crop(formBytes(bytes)!!, aspectWidth, aspectHeight)
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

    fun flipVertical(bitmap: Bitmap?): Bitmap? {
        bitmap ?: return null
        val matrix = Matrix()
        matrix.preScale(1.0f, -1.0f)
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }

    fun flipHorizontal(bitmap: Bitmap?): Bitmap? {
        bitmap ?: return null
        val matrix = Matrix()
        matrix.preScale(-1.0f, 1.0f)
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }


    /**
     *
     */
    fun toBytes(image: Image): ByteArray? {
        var data: ByteArray? = null
        try {
            if (image.format == ImageFormat.JPEG) {
                val planes = image.planes
                val buffer = planes[0].buffer
                data = ByteArray(buffer.capacity())
                buffer.get(data)
                return data
            } else if (image.format == ImageFormat.YUV_420_888) {
                data = nv21toJpeg(yuv420toNv21(image), image.width, image.height, 100)
            }
        } catch (ex: Exception) {
            Log.e("ImagetoByteArray", ex.message)
        }

        return data
    }

    fun toRawBytes(byteArray: ByteArray): ByteArray {
        val rawBitmap = formBytes(byteArray)
        val stream = ByteArrayOutputStream()
        rawBitmap?.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        val rawByteArray = stream.toByteArray()
        rawBitmap?.recycle()
        return rawByteArray
    }

    fun toBytes(bitmap: Bitmap): ByteArray {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream)
        return stream.toByteArray()
    }

    fun base64Encode(data: ByteArray, flag: Int = Base64.DEFAULT): String {
        return Base64.encodeToString(data, flag)
    }


    /**
     * Create bitmap
     */
    fun formRes(@DrawableRes res: Int): Bitmap? {
        return BitmapFactory.decodeResource(Library.app.resources, res)
    }

    fun formView(v: View?, width: Int = v?.width ?: 0, height: Int = v?.height ?: 0): Bitmap? {
        v ?: return null
        if (width > 0 && height > 0) {
            v.measure(View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY), View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY))
        }
        v.layout(0, 0, v.measuredWidth, v.measuredHeight)
        val bitmap = Bitmap.createBitmap(v.measuredWidth, v.measuredHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val background = v.background
        background?.draw(canvas)
        v.draw(canvas)
        return bitmap
    }

    fun formBytes(byteArray: ByteArray): Bitmap? {
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
    }

    fun formBytes(byteArray: ByteArray?, width: Int, height: Int): Bitmap? {
        if (byteArray != null) {
            var options: BitmapFactory.Options? = null
            if (width > 0 && height > 0) {
                options = BitmapFactory.Options()
                options.inJustDecodeBounds = true
                BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size, options)
                options.inJustDecodeBounds = false
                options.inSampleSize = calculateInSampleSize(options, width, height)
            }
            val srcBitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size, options)
            val orientation = orientation(byteArray)
            if (srcBitmap != null && orientation != 0) {
                val matrix = Matrix()
                matrix.postRotate(orientation.toFloat())
                val bitmap = Bitmap.createBitmap(srcBitmap, 0, 0, srcBitmap.width, srcBitmap.height, matrix, true)
                srcBitmap.recycle()
                return bitmap
            }
            return srcBitmap
        }
        return null
    }

    fun formBytes(byteArray: ByteArray, config: Bitmap.Config = Bitmap.Config.ALPHA_8, width: Int, height: Int): Bitmap? {
        val bitmap = Bitmap.createBitmap(width, height, config)
        val buffer = ByteBuffer.wrap(byteArray)
        bitmap.copyPixelsFromBuffer(buffer)
        return bitmap
    }

}