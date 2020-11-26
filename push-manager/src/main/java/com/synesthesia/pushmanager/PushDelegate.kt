//
//  Created by Marco Porcaro on 25/11/2020.
//  Copyright © 2020 Synesthesia. All rights reserved.
//

package com.synesthesia.pushmanager

import com.synesthesia.pushmanager.model.PushToken
import com.synesthesia.pushmanager.model.PushType
import com.synesthesia.pushmanager.model.RemoteMessage

interface PushDelegate {

    fun onNewToken(pushToken: PushToken) {}

    fun onMessageReceived(tokenType: PushType, remoteMessage: RemoteMessage?) {}

    fun onFcmMessageReceived(remoteMessage: com.google.firebase.messaging.RemoteMessage?) {}

    fun onHmsMessageReceived(remoteMessage: com.huawei.hms.push.RemoteMessage?) {}
}