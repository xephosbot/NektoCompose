package com.xbot.socket

import io.socket.client.Socket
import io.socket.emitter.Emitter
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.onFailure
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.serialization.StringFormat
import kotlinx.serialization.serializer
import org.json.JSONObject
import java.io.IOException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class SocketIOConnectionException : IOException()

/**
 * Устанавливает соединение с сервером и приостанавливает выполнение текущей корутины до тех пор,
 * пока не будет получено событие [Socket.EVENT_CONNECT] или [Socket.EVENT_CONNECT_ERROR].
 * При отмене корутины вызывается [Socket.disconnect].
 *
 * @return Сокет [Socket], если соединение установлено успешно.
 * @throws SocketIOConnectionException, если произошла ошибка при подключении.
 */
suspend inline fun Socket.connectAwait(): Socket {
    if (connected()) return this

    return suspendCancellableCoroutine { cont ->
        once(Socket.EVENT_CONNECT) {
            if (cont.isActive) cont.resume(this)
        }
        once(Socket.EVENT_CONNECT_ERROR) {
            if (cont.isActive) cont.resumeWithException(SocketIOConnectionException())
        }

        connect()

        cont.invokeOnCancellation { disconnect() }
    }
}

/**
 * Выполняет вызов [Socket.emit] и затем приостанавливает выполнение текущей корутины до тех пор,
 * пока не будет вызван [io.socket.client.Ack.call].
 *
 * @param event Строка, представляющая имя события.
 * @param args Массив аргументов для передачи событию.
 * @return Массив результатов [io.socket.client.Ack.call].
 */
suspend inline fun Socket.emitAwait(event: String, vararg args: Any): Array<out Any> =
    suspendCoroutine { continuation ->
        emit(event, args) { results ->
            continuation.resume(results)
        }
    }

/**
 * Регистрирует [Emitter.Listener], который будет слушать указанное событие и
 * при вызове этого события, приостанавливает текущую корутину и возобновляет ее,
 * передавая результат в виде массива аргументов.
 * Слушатель будет автоматически удален после выполнения или отмены корутины.
 *
 * @param event Строка, представляющая имя события.
 * @return Массив аргументов, переданных при вызове события.
 */
suspend inline fun Emitter.onceAwait(event: String): Array<out Any> {
    return suspendCancellableCoroutine { continuation ->
        val listener: Emitter.Listener = Emitter.Listener {
            if (continuation.isActive)
                continuation.resume(it)
        }
        continuation.invokeOnCancellation { off(event, listener) }
        once(event, listener)
    }
}

/**
 * Регистрирует [Emitter.Listener], который будет отправлять входящие события
 * в канал, основанный на [Flow]. Если событие не удалось отправить в поток,
 * то слушатель будет удален.
 * Закрытие потока также приведет к удалению слушателя.
 *
 * @param event Строка, представляющая имя события.
 * @return [Flow] для получения входящих событий в виде массивов аргументов.
 */
fun Emitter.onFlow(event: String): Flow<Array<out Any>> = callbackFlow {
    val listener = object : Emitter.Listener {
        override fun call(vararg args: Any) {
            trySendBlocking(args).onFailure {
                off(event, this)
            }
        }
    }

    on(event, listener)

    awaitClose { off(event, listener) }
}

/**
 * Преобразует объект типа [T] в [JSONObject] с использованием сериализатора из [StringFormat].
 * @param value Объект для сериализации.
 * @return [JSONObject], представляющий сериализованный объект.
 */
inline fun <reified T> StringFormat.encodeToJsonObject(value: T): JSONObject =
    JSONObject(encodeToString(serializersModule.serializer(), value))
