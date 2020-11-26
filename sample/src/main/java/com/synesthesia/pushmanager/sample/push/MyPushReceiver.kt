//
//  Created by Marco Porcaro on 25/11/2020.
//  Copyright © 2020 Synesthesia. All rights reserved.
//

package com.synesthesia.pushmanager.sample.push

import android.util.Log
import com.synesthesia.pushmanager.PushDelegate
import com.synesthesia.pushmanager.model.PushToken
import com.synesthesia.pushmanager.model.PushType
import com.synesthesia.pushmanager.model.RemoteMessage

class MyPushReceiver : PushDelegate {

    private val TAG = "MyPushReceiver"

    override fun onNewToken(pushToken: PushToken) {
        Log.d(TAG, pushToken.toString())
    }

    override fun onMessageReceived(tokenType: PushType, remoteMessage: RemoteMessage?) {
        Log.d(TAG, "Message received")
    }
}