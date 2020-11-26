//
//  Created by Marco Porcaro on 25/11/2020.
//  Copyright © 2020 Synesthesia. All rights reserved.
//

package com.synesthesia.pushmanager.sample.view

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.viewModels
import androidx.fragment.app.FragmentActivity
import com.android.app.R
import com.synesthesia.pushmanager.sample.viewmodel.PushTokenViewModel

class PushTokenActivity : FragmentActivity() {


    private val pushTokenViewModel  by viewModels<PushTokenViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_push_token)

        pushTokenViewModel.token.observe(this@PushTokenActivity, {
            findViewById<TextView>(R.id.tv_push_service)?.text = "Push Service: ${it.tokenType.name}"
            findViewById<TextView>(R.id.tv_token)?.text = "Push Token: ${it.token}"
        })


        findViewById<Button>(R.id.btn_get_token)?.setOnClickListener {
            pushTokenViewModel.getTokenWithCoroutine()
        }

    }

}
