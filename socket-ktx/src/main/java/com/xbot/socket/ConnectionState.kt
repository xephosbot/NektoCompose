package com.xbot.socket

import kotlinx.coroutines.flow.distinctUntilChanged

/**
 * Состояние подключения к сокету.
 */
sealed interface ConnectionState {
    /**
     * Установлено подключение к сокету.
     */
    data object Connected : ConnectionState

    /**
     * Подключение к сокету разорвано.
     */
    data object Disconnected : ConnectionState

    /**
     * Ошибка при подключении к сокету.
     *
     * @param cause Причина ошибки.
     */
    data class Error(val cause: Throwable) : ConnectionState
}

fun Client.connectionStateFlow() = connectionState.distinctUntilChanged { old, new ->
    old::class == new::class
}
