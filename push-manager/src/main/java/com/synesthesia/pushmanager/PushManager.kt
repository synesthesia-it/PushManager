//
//  Created by Marco Porcaro on 25/11/2020.
//  Copyright © 2020 Synesthesia. All rights reserved.
//

package com.synesthesia.pushmanager

import android.content.Context
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.firebase.messaging.FirebaseMessaging
import com.huawei.hms.aaid.HmsInstanceId
import com.huawei.hms.api.HuaweiApiAvailability
import com.huawei.hms.push.HmsMessaging
import com.synesthesia.pushmanager.exception.NoTokenAvailableException
import com.synesthesia.pushmanager.exception.PushManagerNotInitializedException
import com.synesthesia.pushmanager.log.Logger
import com.synesthesia.pushmanager.model.PushToken
import com.synesthesia.pushmanager.model.PushType
import io.reactivex.Single
import kotlinx.coroutines.rx2.rxSingle
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


class PushManager {

    private var huaweiAppId: String? = null
    private val TAG = "PushManager"
    internal var delegate: PushDelegate? = null
    private lateinit var context: Context
    internal var logEnabled: Boolean = false
    private var pushServiceEnabled: PushType? = null


    companion object {

        val instance by lazy { PushManager() }

        fun isGooglePlayServicesAvailable(context: Context): Boolean {
            val status = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(context)
            return status == ConnectionResult.SUCCESS
        }

        fun isHuaweiMobileServicesAvailable(context: Context, minApkVersion: Int? = null): Boolean {
            val status = HuaweiApiAvailability.getInstance().isHuaweiMobileServicesAvailable(context)
            return status == ConnectionResult.SUCCESS
        }
    }


    fun init(context: Context,
             delegate: PushDelegate? = null,
             huaweiAppId: String? = null,
             preferredPushService: PushType = PushType.FIREBASE_MESSAGING_SERVICE,
             enableLog: Boolean = false
    ) {
        instance.delegate = delegate
        instance.context = context
        instance.logEnabled = enableLog

        when {
            isGooglePlayServicesAvailable(context) && isHuaweiMobileServicesAvailable(context) -> {
                when (preferredPushService) {
                    PushType.FIREBASE_MESSAGING_SERVICE -> {
                        HmsMessaging.getInstance(context).isAutoInitEnabled = false
                        FirebaseMessaging.getInstance().isAutoInitEnabled = true
                        instance.pushServiceEnabled = PushType.FIREBASE_MESSAGING_SERVICE
                        Logger.d(TAG, "FirebaseMessaging enabled")
                    }
                    PushType.HUAWEI_PUSH_KIT -> {
                        if (huaweiAppId.isNullOrEmpty()) {
                            Logger.e(TAG, "Cannot enable HmsMessaging without huaweiAppId")
                            return
                        }
                        instance.huaweiAppId = huaweiAppId
                        HmsMessaging.getInstance(context).isAutoInitEnabled = true
                        FirebaseMessaging.getInstance().isAutoInitEnabled = false
                        instance.pushServiceEnabled = PushType.HUAWEI_PUSH_KIT
                        Logger.d(TAG, "HmsMessaging enabled")
                    }
                }

            }
            isGooglePlayServicesAvailable(context) -> {
                FirebaseMessaging.getInstance().isAutoInitEnabled = true
                instance.pushServiceEnabled = PushType.FIREBASE_MESSAGING_SERVICE
                Logger.d(TAG, "FirebaseMessaging enabled")
            }
            isHuaweiMobileServicesAvailable(context) -> {
                if (huaweiAppId.isNullOrEmpty()) {
                    Logger.e(TAG, "Cannot enable HmsMessaging without huaweiAppId")
                    return
                }
                instance.huaweiAppId = huaweiAppId
                HmsMessaging.getInstance(context).isAutoInitEnabled = true
                instance.pushServiceEnabled = PushType.HUAWEI_PUSH_KIT
                Logger.d(TAG, "HmsMessaging enabled")
            }
            else -> {
                Logger.e(TAG, "NO PUSH SERVICES FOUND THAT MATCHES THE REQUIREMENTS")
            }
        }

    }

    fun getPushServiceEnabled(): PushType? {
        return pushServiceEnabled
    }

    fun getTokenRx(): Single<PushToken> {
        return rxSingle { getToken() }
    }

    suspend fun getToken(): PushToken {
        return when (instance.pushServiceEnabled) {
            PushType.FIREBASE_MESSAGING_SERVICE -> {
                PushToken(token = getFcmTokenAsync(), tokenType = PushType.FIREBASE_MESSAGING_SERVICE)
            }
            PushType.HUAWEI_PUSH_KIT -> {
                PushToken(token = getHuaweiTokenAsync(instance.context), tokenType = PushType.HUAWEI_PUSH_KIT)
            }
            else -> {
                throw NoTokenAvailableException()
            }
        }
    }

    fun subscribeToTopic(topic: String) {
        when (instance.pushServiceEnabled) {
            PushType.FIREBASE_MESSAGING_SERVICE -> {
                FirebaseMessaging.getInstance().subscribeToTopic(topic)
            }
            PushType.HUAWEI_PUSH_KIT -> {
                HmsMessaging.getInstance(instance.context).subscribe(topic)
            }
            else -> {
                throw PushManagerNotInitializedException()
            }
        }
    }

    fun unsubscribeFromTopic(topic: String) {
        when (instance.pushServiceEnabled) {
            PushType.FIREBASE_MESSAGING_SERVICE -> {
                FirebaseMessaging.getInstance().unsubscribeFromTopic(topic)
            }
            PushType.HUAWEI_PUSH_KIT -> {
                HmsMessaging.getInstance(instance.context).unsubscribe(topic)
            }
            else -> {
                throw PushManagerNotInitializedException()
            }
        }
    }



    fun deleteFcmToken() {
        if (instance.pushServiceEnabled == PushType.FIREBASE_MESSAGING_SERVICE) {
            FirebaseMessaging.getInstance().deleteToken()
        }
    }

    fun turnOffHmsPush() {
        if (instance.pushServiceEnabled == PushType.HUAWEI_PUSH_KIT) {
            HmsMessaging.getInstance(instance.context).turnOffPush()
        }
    }

    fun turnOnHmsPush() {
        if (instance.pushServiceEnabled == PushType.HUAWEI_PUSH_KIT) {
            HmsMessaging.getInstance(instance.context).turnOnPush()
        }
    }



    ////////////////////////////////////////////////////////////////////////////////
    //
    //                                  PRIVATE
    //
    ////////////////////////////////////////////////////////////////////////////////



    private suspend fun getFcmTokenAsync(): String? {
        return suspendCoroutine { continuation ->
            FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Logger.w(TAG, "Get FCM token failed", task.exception)
                }
                val token = task.result
                Logger.d(TAG, "FCM token: $token")
                continuation.resume(token)
            }
        }
    }

    private fun getHuaweiTokenAsync(context: Context): String? {
        var token: String? = null
        try {
            token = HmsInstanceId.getInstance(context).getToken(instance.huaweiAppId, HmsMessaging.DEFAULT_TOKEN_SCOPE)
            Logger.d(TAG, "HMS token: $token")
        } catch (e: Exception) {
            Logger.i(TAG, "getToken failed.")
        }
        return token
    }

}
