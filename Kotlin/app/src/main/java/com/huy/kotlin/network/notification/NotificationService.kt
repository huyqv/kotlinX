package com.huy.kotlin.network.notification

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2019/04/19
 * @Description: ...
 * All Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
class NotificationService : FirebaseMessagingService() {

    override fun onMessageReceived(message: RemoteMessage?) {
        NotificationHelper.instance.notify(message)
    }

    override fun onNewToken(token: String?) {

    }

}
