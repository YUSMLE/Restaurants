package com.yusmle.restaurants.common.helper.util

import android.os.Handler
import android.view.View

fun <T : View> T.onClickThrottled(skipDurationMillis: Long = 750, action: (view: T) -> Unit) {
    var isEnabled = true
    this.setOnClickListener {
        if (isEnabled) {
            action(this)
            isEnabled = false
            Handler().postDelayed({ isEnabled = true }, skipDurationMillis)
        }
    }
}
