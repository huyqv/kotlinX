package com.huy.library.util

import android.annotation.TargetApi
import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.text.TextUtils
import java.io.*
import java.net.URL

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2018/10/28
 * @Description: ...
 * None Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
object BitmapUtil {

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
    fun getBitmapSize(bitmap: Bitmap): Int {
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

    fun saveBitmap(context: Context, bitmap: Bitmap, directory: String?, filename: String, compressConfigs: CompressConfigs): File? {

        var fDirectory = directory
        var fFilename = filename

        if (fDirectory == null) {
            fDirectory = context.cacheDir.absolutePath
        } else {
            // Check if the given directory exists or try to create it.
            val file = File(fDirectory)
            if (!file.isDirectory && !file.mkdirs()) {
                return null
            }
        }

        val byteCount = getBitmapSize(bitmap).toLong()

        val max = compressConfigs.maxSize
        var compressRatio = 100
        if (byteCount > max) {
            compressRatio = (100.0f * max / byteCount).toInt()
        }

        var file: File? = null
        var os: OutputStream? = null
        try {
            val format = compressConfigs.compressFormat
            fFilename = fFilename + compressConfigs.extension
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

    fun saveBitmap(context: Context, bitmap: Bitmap, parentDir: File, fileName: String, compressConfigs: CompressConfigs): File? {

        return saveBitmap(context, bitmap, parentDir.absolutePath, fileName, compressConfigs)
    }

    fun getImageUri(context: Context, format: Bitmap.CompressFormat, quality: Int, bitmap: Bitmap?): Uri? {
        if (bitmap == null || bitmap.isRecycled) {
            return null
        }
        try {
            val outputStream = ByteArrayOutputStream()
            bitmap.compress(format, quality, outputStream)

            val path = MediaStore.Images.Media.insertImage(context.contentResolver, bitmap, "", null)
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

    /**
     * [CompressConfigs]
     */
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
}