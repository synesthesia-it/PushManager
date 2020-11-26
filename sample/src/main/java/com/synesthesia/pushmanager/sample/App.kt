//
//  Created by Marco Porcaro on 25/11/2020.
//  Copyright © 2020 Synesthesia. All rights reserved.
//

package com.synesthesia.pushmanager.sample

import android.app.Application
import com.synesthesia.pushmanager.PushManager
import com.synesthesia.pushmanager.sample.push.MyPushReceiver
import com.synesthesia.pushmanager.model.PushType


open class App : Application() {


    private val myPushReceiver by lazy { MyPushReceiver() }


    override fun onCreate() {
        super.onCreate()

        PushManager.instance.init(
                context = applicationContext,
                delegate = myPushReceiver,
                huaweiAppId = "MY_HUAWEI_APP_ID",
                preferredPushService = PushType.FIREBASE_MESSAGING_SERVICE,
                enableLog = false)
    }


}