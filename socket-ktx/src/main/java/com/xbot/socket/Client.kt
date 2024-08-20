package com.xbot.socket

import io.socket.client.IO
import io.socket.client.Socket
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import com.xbot.socket.request.Request
import com.xbot.socket.update.Update

/**
 * Клиент для подключения к сокету и обмена данными.
 *
 * @param options Настройки подключения к сокету.
 */
class Client(
    options: IO.Options = IO.Options().apply { secure = true }
) {
    private val socket: Socket = IO.socket("https://im.nekto.me", options)

    @OptIn(ExperimentalSerializationApi::class)
    private val format = Json {
        ignoreUnknownKeys = true
        explicitNulls = false
        encodeDefaults = true
    }

    /**
     * Flow, который эмитирует события подключения [ConnectionState] к сокету.
     * События подключения получаются из событий [Socket.EVENT_CONNECT], [Socket.EVENT_DISCONNECT] и [Socket.EVENT_CONNECT_ERROR].
     */
    val connectionState: Flow<ConnectionState> = merge(
        socket.onFlow(Socket.EVENT_CONNECT).map { ConnectionState.Connected },
        socket.onFlow(Socket.EVENT_DISCONNECT).map { ConnectionState.Disconnected },
        socket.onFlow(Socket.EVENT_CONNECT_ERROR).map { args ->
            ConnectionState.Error(args[0] as Throwable)
        }
    )

    /**
     * Flow, который эмитирует обновления [Update] сокета.
     * Обновления получаются из события "notice".
     */
    val update: Flow<Update> = socket.onFlow("notice")
        .map { args -> format.decodeFromString<Update>(args[0].toString()) }

    /**
     * Отправляет действие (запрос) на сервер.
     *
     * @param request Запрос для отправки.
     */
    suspend fun emitAction(request: Request) {
        socket.emitAwait("action", format.encodeToJsonObject(request))
    }

    suspend fun connect() {
        try {
            socket.connectAwait()
        } catch (e: SocketIOConnectionException) {
            //TODO: Error handling
        }
    }
}
