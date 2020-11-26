//
//  Created by Marco Porcaro on 25/11/2020.
//  Copyright © 2020 Synesthesia. All rights reserved.
//

package com.synesthesia.pushmanager.log

import android.util.Log
import com.synesthesia.pushmanager.PushManager

object Logger {

    fun e(tag: String, message: String, t: Throwable? = null) {
        if (PushManager.instance.logEnabled) {
            Log.e(tag, message, t)
        }
    }

    fun d(tag: String, message: String, t: Throwable? = null) {
        if (PushManager.instance.logEnabled) {
            Log.d(tag, message, t)
        }
    }

    fun i(tag: String, message: String, t: Throwable? = null) {
        if (PushManager.instance.logEnabled) {
            Log.i(tag, message, t)
        }
    }

    fun w(tag: String, message: String, t: Throwable? = null) {
        if (PushManager.instance.logEnabled) {
            Log.w(tag, message, t)
        }
    }

}