//
//  Created by Marco Porcaro on 25/11/2020.
//  Copyright © 2020 Synesthesia. All rights reserved.
//

package com.synesthesia.pushmanager.model

import android.net.Uri


data class PushNotification(
        val body: String?,
        val bodyLocalizationArgs: Array<String>?,
        val bodyLocalizationKey: String?,
        val channelId: String?,
        val clickAction: String?,
        val color: String?,
        val defaultLightSettings: Boolean,
        val defaultSound: Boolean,
        val defaultVibrateSettings: Boolean,
        val eventTime: Long?,
        val icon: String?,
        val imageUrl: Uri?,
        val lightSettings: IntArray?,
        val link: Uri?,
        val localOnly: Boolean,
        val notificationCount: Int?,
        val notificationPriority: Int?,
        val sound: String?,
        val sticky: Boolean,
        val tag: String?,
        val ticker: String?,
        val title: String?,
        val titleLocalizationArgs: Array<String>?,
        val titleLocalizationKey: String?,
        val vibrateTiming: LongArray?,
        val visibility: Int?
)
