package com.xbot.data

import com.xbot.socket.ConnectionState

fun ConnectionState.mapToDomainModel(): com.xbot.domain.model.ConnectionState {
    return when (this) {
        is ConnectionState.Connected -> com.xbot.domain.model.ConnectionState.Connected
        is ConnectionState.Disconnected -> com.xbot.domain.model.ConnectionState.Disconnected
        is ConnectionState.Error -> com.xbot.domain.model.ConnectionState.Error(this.cause)
    }
}