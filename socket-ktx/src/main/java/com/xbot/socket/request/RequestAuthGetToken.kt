package com.xbot.socket.request

import com.xbot.socket.Client
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

suspend fun Client.authGetToken(
    deviceType: Int,
    deviceId: String,
    pushToken: String,
    deviceName: String
) = emitAction(
    RequestAuthGetToken(
        deviceType = deviceType,
        deviceId = deviceId,
        push = pushToken,
        deviceName = deviceName
    )
)

@Serializable
@SerialName("auth.getToken")
data class RequestAuthGetToken(
    val deviceType: Int,
    val deviceId: String,
    val push: String,
    val deviceName: String
) : Request()
