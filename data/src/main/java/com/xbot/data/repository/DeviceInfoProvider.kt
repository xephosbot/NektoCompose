package com.xbot.data.repository

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.provider.Settings
import javax.inject.Inject

class DeviceInfoProvider @Inject constructor(
    context: Context
) {
    val deviceType: Int = 3

    @SuppressLint("HardwareIds")
    val deviceId: String = Settings.Secure.getString(
        context.contentResolver,
        Settings.Secure.ANDROID_ID
    )

    val deviceName: String = "${Build.MANUFACTURER} ${Build.MODEL}"
}