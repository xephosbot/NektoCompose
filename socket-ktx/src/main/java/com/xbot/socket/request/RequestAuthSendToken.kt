package com.xbot.socket.request

import com.xbot.socket.Client
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

suspend fun Client.authSendToken(
    token: String,
    pushToken: String,
    deviceId: String
) = emitAction(
    RequestAuthSendToken(
        token = token,
        pushToken = pushToken,
        deviceId = deviceId
    )
)

@Serializable
@SerialName("auth.sendToken")
data class RequestAuthSendToken(
    val token: String,
    val pushToken: String,
    val deviceId: String
) : Request()
