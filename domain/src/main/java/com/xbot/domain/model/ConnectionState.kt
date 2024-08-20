package com.xbot.domain.model

sealed class ConnectionState {
    data object Connected : ConnectionState()
    data object Disconnected : ConnectionState()
    data class Error(val cause: Throwable) : ConnectionState()
}