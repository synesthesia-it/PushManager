//
//  Created by Marco Porcaro on 25/11/2020.
//  Copyright © 2020 Synesthesia. All rights reserved.
//

package com.synesthesia.pushmanager.model

import android.content.Intent


data class RemoteMessage(
        val notification: PushNotification?,
        val collapseKey: String?,
        val data: MutableMap<String, String>,
        val from: String?,
        val messageId: String?,
        val messageType: String?,
        val originalPriority: Priority,
        val priority: Priority,
        val sentTime: Long,
        val to: String?,
        val ttl: Int,
        val toIntent: Intent?
) {
    enum class Priority {
        HIGH,
        NORMAL,
        UNKNOWN
    }
}



fun com.google.firebase.messaging.RemoteMessage?.toPushRemoteMessage(): RemoteMessage? {
    if(this == null){
        return null
    }
    else {
        return RemoteMessage(
                notification = PushNotification(
                        body = this.notification?.body,
                        bodyLocalizationArgs = this.notification?.bodyLocalizationArgs,
                        bodyLocalizationKey = this.notification?.bodyLocalizationKey,
                        channelId = this.notification?.channelId,
                        clickAction = this.notification?.clickAction,
                        color = this.notification?.color,
                        defaultLightSettings = this.notification?.defaultLightSettings ?: false,
                        defaultSound = this.notification?.defaultSound ?: false,
                        defaultVibrateSettings = this.notification?.defaultVibrateSettings ?: false,
                        eventTime = this.notification?.eventTime,
                        icon = this.notification?.icon,
                        imageUrl = this.notification?.imageUrl,
                        lightSettings = this.notification?.lightSettings,
                        link = this.notification?.link,
                        localOnly = this.notification?.localOnly ?: false,
                        notificationCount = this.notification?.notificationCount,
                        notificationPriority = this.notification?.notificationPriority,
                        sound = this.notification?.sound,
                        sticky = this.notification?.sticky ?: false,
                        tag = this.notification?.tag,
                        ticker = this.notification?.ticker,
                        title = this.notification?.title,
                        titleLocalizationArgs = this.notification?.titleLocalizationArgs,
                        titleLocalizationKey = this.notification?.titleLocalizationKey,
                        vibrateTiming = this.notification?.vibrateTimings,
                        visibility = this.notification?.visibility
                ),
                collapseKey = this.collapseKey,
                data = this.data,
                from = this.from,
                messageId = this.messageId,
                messageType = this.messageType,
                originalPriority = fcmPriorityToPriority(this.originalPriority),
                priority = fcmPriorityToPriority(this.priority),
                sentTime = this.sentTime,
                to = this.to,
                ttl = this.ttl,
                toIntent = this.toIntent()
        )
    }
}

internal fun fcmPriorityToPriority(priority: Int): RemoteMessage.Priority {
    return when(priority) {
        com.google.firebase.messaging.RemoteMessage.PRIORITY_HIGH -> RemoteMessage.Priority.HIGH
        com.google.firebase.messaging.RemoteMessage.PRIORITY_NORMAL -> RemoteMessage.Priority.NORMAL
        else -> RemoteMessage.Priority.UNKNOWN
    }
}

fun com.huawei.hms.push.RemoteMessage?.toPushRemoteMessage(): RemoteMessage? {
    if(this == null){
        return null
    }
    else {
        return RemoteMessage(
                notification = PushNotification(
                        body = this.notification?.body,
                        bodyLocalizationArgs = this.notification?.bodyLocalizationArgs,
                        bodyLocalizationKey = this.notification?.bodyLocalizationKey,
                        channelId = this.notification?.channelId,
                        clickAction = this.notification?.clickAction,
                        color = this.notification?.color,
                        defaultLightSettings = this.notification?.isDefaultLight ?: false,
                        defaultSound = this.notification?.isDefaultSound ?: false,
                        defaultVibrateSettings = this.notification?.isDefaultVibrate ?: false,
                        eventTime = null,
                        icon = this.notification?.icon,
                        imageUrl = this.notification?.imageUrl,
                        lightSettings = this.notification?.lightSettings,
                        link = this.notification?.link,
                        localOnly = this.notification?.isLocalOnly ?: false,
                        notificationCount = this.notification?.badgeNumber,
                        notificationPriority = this.notification?.importance,
                        sound = this.notification?.sound,
                        sticky = !this.notification.isAutoCancel,
                        tag = this.notification?.tag,
                        ticker = this.notification?.ticker,
                        title = this.notification?.title,
                        titleLocalizationArgs = this.notification?.titleLocalizationArgs,
                        titleLocalizationKey = this.notification?.titleLocalizationKey,
                        vibrateTiming = this.notification?.vibrateConfig,
                        visibility = this.notification?.visibility
                ),
                collapseKey = this.collapseKey,
                data = this.dataOfMap,
                from = this.from,
                messageId = this.messageId,
                messageType = this.messageType,
                originalPriority = hmsUrgencyToPriority(this.originalUrgency),
                priority = hmsUrgencyToPriority(this.originalUrgency),
                sentTime = this.sentTime,
                to = this.to,
                ttl = this.ttl,
                toIntent = null
        )
    }
}

internal fun hmsUrgencyToPriority(urgency: Int): RemoteMessage.Priority {
    return when(urgency) {
        com.huawei.hms.push.RemoteMessage.PRIORITY_HIGH -> RemoteMessage.Priority.HIGH
        com.huawei.hms.push.RemoteMessage.PRIORITY_NORMAL -> RemoteMessage.Priority.NORMAL
        else -> RemoteMessage.Priority.UNKNOWN
    }
}

