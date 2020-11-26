//
//  Created by Marco Porcaro on 25/11/2020.
//  Copyright © 2020 Synesthesia. All rights reserved.
//

package com.synesthesia.pushmanager.model


data class PushToken(
        val token: String?,
        val tokenType: PushType) {

    fun isValid() = !token.isNullOrEmpty()

    override fun toString() = "PushService: ${tokenType.name} - PushToken: ${token}"
}