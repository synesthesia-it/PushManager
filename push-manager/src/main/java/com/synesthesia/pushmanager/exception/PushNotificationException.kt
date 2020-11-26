//
//  Created by Marco Porcaro on 25/11/2020.
//  Copyright © 2020 Synesthesia. All rights reserved.
//

package com.synesthesia.pushmanager.exception

sealed class PushNotificationException: RuntimeException()

class NoTokenAvailableException() : PushNotificationException()
class PushManagerNotInitializedException() : PushNotificationException()