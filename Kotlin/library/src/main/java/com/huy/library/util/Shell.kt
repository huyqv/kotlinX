package com.huy.library.util

import android.util.Log
import com.huy.library.BuildConfig
import java.io.*
import java.net.DatagramSocket
import java.net.Socket
import java.util.*

object Shell {

    private const val TAG = "Shell"

    fun exec(vararg strings: String) {
        try {
            val su = Runtime.getRuntime().exec("su")
            val outputStream = DataOutputStream(su.outputStream)
            for (s in strings) {
                outputStream.writeBytes("""
    $s

    """.trimIndent())
                outputStream.flush()
            }
            outputStream.writeBytes("exit\n")
            outputStream.flush()
            try {
                su.waitFor()
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
            outputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    //---- value isAuto is 1 or 0
    fun autoUpdateTime(isAuto: Int) {
        exec("settings put global auto_time $isAuto")
    }

    fun checkAutoUpdateTime(): String {
        return execForResult("settings get global auto_time")
    }

    fun changeTimeZone(timeZone: String) {
        exec("setprop persist.sys.timezone \"$timeZone\"")
    }

    fun disableSetting() {
        exec("pm disable com.android.settings")
    }

    fun enableSetting() {
        exec("pm enable com.android.settings")
    }

    fun fullScreenMode() {
        exec("settings put global policy_control immersive.full=*")
    }

    fun hideNavigationBar() {
        exec("wm overscan 0,-90,0,-45")
    }

    fun showNavigationBar() {
        exec("wm overscan 0,0,0,0")
    }

    fun disableMainLauncher() {
        exec("pm disable com.android.launcher3")
    }

    fun enableMainLauncher() {
        exec("pm enable com.android.launcher3")
    }

    val folderApp: String
        get() {
            val string = execForResult("ls /data/app")
            val list = string.split("\n").toTypedArray()
            val i = 0
            var folder = ""
            val appID: String = BuildConfig.APPLICATION_ID
            for (value in list) {
                if (value.contains(appID)) {
                    folder = value
                    break
                }
            }
            return "/data/app/$folder"
        }

    @Throws(IOException::class)
    fun updateApp(pathAPK: String): Boolean {
        val folderApp = folderApp
        execForResult("push $pathAPK $folderApp")
        return true
    }

    @Throws(IOException::class)
    fun startADB(port: Int) {
        val cmds = arrayOf(
                "setprop service.adb.tcp.port $port",
                "stop adbd",
                "start adbd"
        )
        var result = execForResult("getprop service.adb.tcp.port")
        Log.i(TAG, "Starting ADB, current port = $result")

        // TCP not enabled (frist time)
        if (result == null || !result.contains(Integer.toString(port))) {
            exec(*cmds)
            return
        }

        // ADB.D not running
        result = execForResult("getprop init.svc.adbd")
        if (result == null || !result.contains("running")) {
            exec(*cmds)
        }
    }

    @Throws(IOException::class)
    fun execScript(input: InputStream?) {
        val reader = BufferedReader(InputStreamReader(input))
        val lines: List<String> = LinkedList()
        var line: String?
        while (reader.readLine().also { line = it } != null) {
            //lines.add(line);
            exec(line!!)
        }
        //exec(lines.toArray(new String[]{}));
    }

    fun execForResult(vararg strings: String): String {
        var res = ""
        var outputStream: DataOutputStream? = null
        var response: InputStream? = null
        try {
            val su = Runtime.getRuntime().exec("su")
            outputStream = DataOutputStream(su.outputStream)
            response = su.inputStream
            for (s in strings) {
                outputStream.writeBytes("""
    $s

    """.trimIndent())
                outputStream.flush()
            }
            outputStream.writeBytes("exit\n")
            outputStream.flush()
            try {
                su.waitFor()
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
            res = readFully(response)
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            closeSilently(outputStream, response)
        }
        return res
    }

    @Throws(IOException::class)
    private fun readFully(`is`: InputStream?): String {
        val baos = ByteArrayOutputStream()
        val buffer = ByteArray(1024)
        var length = 0
        while (`is`!!.read(buffer).also { length = it } != -1) {
            baos.write(buffer, 0, length)
        }
        return baos.toString("UTF-8")
    }

    fun closeSilently(vararg xs: Any?) {
        // Note: on Android API levels prior to 19 Socket does not implement Closeable
        for (x in xs) {
            if (x != null) {
                try {
                    if (x is Closeable) {
                        x.close()
                    } else if (x is Socket) {
                        x.close()
                    } else if (x is DatagramSocket) {
                        x.close()
                    } else {
                        throw RuntimeException("cannot close $x")
                    }
                } catch (e: Throwable) {
                    Log.e(TAG, e.message, e)
                }
            }
        }
    }
}