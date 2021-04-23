# PushManager
An Android library that adds support to Firebase Messaging and Huawei Push Kit with a single implementation

At the moment the supported services are Firebase Cloud Messaging (FCM) and Huawei Push Kit (HMS).

# Setup
1.  In the build.gradle of your project, inside *allprojects* -> *repositories*, add
```groovy
maven {url 'http://developer.huawei.com/repo/'}
```

2. In the build.gradle of your project, inside *buildscript* -> *dependencies*, add the latest version of gms and huawei agconnect gradle plugin:
```groovy
classpath 'com.google.gms:google-services:4.3.4'
classpath 'com.huawei.agconnect:agcp:1.4.1.300'
```

3. In the build.gradle of your app module, apply both the plugins at the top of the file
```groovy
apply plugin: 'com.google.gms.google-services'
apply plugin: 'com.huawei.agconnect'
```

4. In the app folder add the **google-services.json** file provided by the firebase console and the **agconnect-services.json** file provided by the Huawei AppGallery console.

6.  In the build.gradle of your app, inside *buildscript* -> *repositories*, add
```groovy
maven { url 'https://jitpack.io' }
```

6. Add the dependecy of the latest version of Handroix push notification library.
```groovy
implementation 'com.github.synesthesia-it:PushManager:[VERSION]'
```


# Basic Implementation
1. In your Application class initialize your push receiver
```kotlin
    private val myPushReceiver by lazy { MyPushReceiver() }
```

2. In the **onCreate()** method of your application, initialize the library
```kotlin
        PushManager.instance.init(
                context = applicationContext,
                delegate = myPushReceiver,
                huaweiAppId = "MY_HUAWEI_APP_ID",
                preferredPushService = PushType.FIREBASE_MESSAGING_SERVICE,
                enableLog = false)
```

Notes:
- the huaweiAppId is not mandatory to receive the push messages and the onNewToken callback, but it's mandatory if you want to retrieve the user token manually.
- preferredPushService is used only if both services are supported by the device

&nbsp;

2. The PushDelegate interface has four methods that you can ovverride:

```kotlin
    fun onNewToken(pushToken: PushToken) {}

    fun onMessageReceived(tokenType: PushType, remoteMessage: RemoteMessage?) {}

    fun onFcmMessageReceived(remoteMessage: com.google.firebase.messaging.RemoteMessage?) {}

    fun onHmsMessageReceived(remoteMessage: com.huawei.hms.push.RemoteMessage?) {}
```
- The onNewToken function is called every time a new token is generated (by FCM or HMS). The PushToken parameter specifies the type (FCM or HMS) and the value.
- The onMessageRecieved funcion is called every time a push notification is received  (by FCM or HMS). You will receive the type (FCM or HMS) and the RemoteMessage. 
The RemoteMessage is a convenient class with both the FCM or the HMS RemoteMessage classes converted in a unique type.
- If for some reason you need to access directly to the RemoteMessage class of the FCM library or HMS library, you can override the specific method. Note that every time a FCM push is received, it will call both the onMessageReceived and the onFcmMessageReceived functions. The same happens for HMS.


## Example of implementation

```kotlin
class MyPushReceiver : PushDelegate {
    override fun onNewToken(pushToken: PushToken) {
        Log.d(TAG, pushToken.toString())
    }

    override fun onMessageReceived(tokenType: PushType, remoteMessage: RemoteMessage?) {
        Log.d(TAG, "Message received")
    }
}
```

# Other features
1. If you need to access the push token in your code, without to wait for a onNewToken callback, you can call one of the two asyncronous methods in the library.

If you use the coroutines:
```kotlin
PushManager.instance.getToken()
```

If you use rx:
```kotlin
PushManager.instance.getTokenRx()
```
Important: On Huawei devices with EMUI <10, this method will NOT respond with any token, but you will receive it in the onNewToken callback in the PushDelegate interface.

2. If you need to know if GooglePlayServices or HuaweiMobileServices are available on the device you can call
```kotlin
PushManager.isGooglePlayServicesAvailable(context)
PushManager.isHuaweiMobileServicesAvailable(context)
```


