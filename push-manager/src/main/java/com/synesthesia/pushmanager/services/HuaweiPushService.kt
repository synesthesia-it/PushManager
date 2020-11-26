//
//  Created by Marco Porcaro on 25/11/2020.
//  Copyright © 2020 Synesthesia. All rights reserved.
//

package com.synesthesia.pushmanager.services

import com.huawei.hms.push.HmsMessageService
import com.huawei.hms.push.RemoteMessage
import com.synesthesia.pushmanager.PushManager
import com.synesthesia.pushmanager.log.Logger
import com.synesthesia.pushmanager.model.PushToken
import com.synesthesia.pushmanager.model.PushType
import com.synesthesia.pushmanager.model.toPushRemoteMessage

class HuaweiPushService : HmsMessageService() {

    private val TAG = "HuaweiPushService"

    override fun onNewToken(token: String?) {
        super.onNewToken(token)
        Logger.d(TAG, "onNewToken: $token")

        PushManager.instance.delegate?.onNewToken(PushToken(token, PushType.HUAWEI_PUSH_KIT))
    }

    /**
     * This method is triggered only if the notification sent is of "data" type
     *
     * @param remoteMessage
     */
    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        super.onMessageReceived(remoteMessage)
        Logger.d(TAG, "HMS message received")

        PushManager.instance.delegate?.onHmsMessageReceived(remoteMessage)

        PushManager.instance.delegate?.onMessageReceived(PushType.HUAWEI_PUSH_KIT, remoteMessage.toPushRemoteMessage())

    }
}