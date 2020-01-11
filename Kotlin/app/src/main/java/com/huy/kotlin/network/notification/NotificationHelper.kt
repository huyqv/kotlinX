package com.huy.kotlin.network.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.RemoteMessage
import com.huy.kotlin.R
import com.huy.kotlin.app.App
import com.huy.kotlin.data.Shared
import com.huy.library.extension.string

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2019/04/19
 * @Description: ...
 * None Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
class NotificationHelper {

    private val manager: NotificationManager
        get() = App.instance.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager


    /**
     * Register/Unregister device token for notification push service
     */
    fun registerToken() {
        FirebaseInstanceId.getInstance().instanceId.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val token = task.result!!.token
                Shared.instance.edit { putString("device_token", token) }
                //registerToken(task.result!!.token)
            }
        }
    }

    fun registerToken(deviceToken: String) {

    }

    fun unRegisterToken(block: (String) -> Unit) {

        val token = Shared.instance.str(TOKEN_KEY)

        if (!token.isNullOrEmpty()) {
            unRegisterToken(token, block)
            return
        }
        FirebaseInstanceId.getInstance().instanceId.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                unRegisterToken(task.result!!.token, block)
            }
        }
    }

    fun unRegisterToken(deviceToken: String, block: (String) -> Unit) {
    }

    /**
     * Local message helper
     */
    fun notify(message: RemoteMessage?) {

        val ntf = message?.notification ?: return

        val data = message.data ?: return

        if (data.isEmpty()) return

        val title = ntf.title

        val body = ntf.body

        val requestID = System.currentTimeMillis().toInt()

        val intent = getIntent(requestID, data) ?: return

        val localNotification = getNotification(title, body)
                .setContentIntent(intent)
                .build()

        instance.manager.notify(requestID, localNotification)

    }

    private fun getIntent(id: Int, data: Map<String, String>): PendingIntent? {

        val notificationId = data[NOTIFICATION_ID] ?: return null

        val intent = Intent(App.instance, NotificationActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.putExtra(NOTIFICATION_ID, notificationId)

        return PendingIntent.getActivity(App.instance, id, intent, PendingIntent.FLAG_ONE_SHOT)
    }

    private fun getNotification(title: String?, body: String?): NotificationCompat.Builder {


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH)
            channel.lightColor = Color.GREEN
            channel.enableLights(true)
            channel.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
            instance.manager.createNotificationChannel(channel)
        }

        return NotificationCompat.Builder(App.instance, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title ?: string(R.string.app_name))
                .setContentText(body ?: string(R.string.notification))
                .setCategory(NotificationCompat.CATEGORY_TRANSPORT)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setColor(ContextCompat.getColor(App.instance, R.color.colorPrimary))
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setShowWhen(false)
                .setColorized(true)
                .setAutoCancel(true)
    }

    companion object {

        const val TOKEN_KEY = "notification_token"

        const val CHANNEL_ID = "default"

        const val CHANNEL_NAME = "default"

        const val NOTIFICATION_ID = "id"

        val instance: NotificationHelper by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            NotificationHelper()
        }

    }

}
