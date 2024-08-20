package com.xbot.socket.request

import com.xbot.socket.Client
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

suspend fun Client.onlineTrack(
    on: Boolean
) = emitAction(RequestOnlineTrack(on))

@Serializable
@SerialName("online.track")
data class RequestOnlineTrack(val on: Boolean) : Request()
