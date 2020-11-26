//
//  Created by Marco Porcaro on 25/11/2020.
//  Copyright © 2020 Synesthesia. All rights reserved.
//

package com.synesthesia.pushmanager.services

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.synesthesia.pushmanager.PushManager
import com.synesthesia.pushmanager.log.Logger
import com.synesthesia.pushmanager.model.PushToken
import com.synesthesia.pushmanager.model.PushType
import com.synesthesia.pushmanager.model.toPushRemoteMessage

class FcmPushService : FirebaseMessagingService() {

    private val TAG = "FcmPushService"

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Logger.d(TAG, "onNewToken: $token")

        PushManager.instance.delegate?.onNewToken(PushToken(token, PushType.FIREBASE_MESSAGING_SERVICE))

    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        Logger.d(TAG, "FCM message received")

        PushManager.instance.delegate?.onFcmMessageReceived(remoteMessage)

        PushManager.instance.delegate?.onMessageReceived(PushType.FIREBASE_MESSAGING_SERVICE, remoteMessage.toPushRemoteMessage())
    }
}