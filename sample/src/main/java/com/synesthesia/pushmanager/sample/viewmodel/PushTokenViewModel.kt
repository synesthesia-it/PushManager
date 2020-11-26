//
//  Created by Marco Porcaro on 25/11/2020.
//  Copyright © 2020 Synesthesia. All rights reserved.
//

package com.synesthesia.pushmanager.sample.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.synesthesia.pushmanager.PushManager
import com.synesthesia.pushmanager.model.PushToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

open class PushTokenViewModel: ViewModel(), CoroutineScope {

    val token: MutableLiveData<PushToken> = MutableLiveData()


    fun getTokenWithCoroutine() {
        launch {
            val token = withContext(Dispatchers.IO) {
                PushManager.instance.getToken()
            }
            this@PushTokenViewModel.token.postValue(token)
        }
    }

    private var job: Job = Job()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }


}
