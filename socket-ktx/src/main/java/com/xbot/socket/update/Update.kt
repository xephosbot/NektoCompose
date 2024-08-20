package com.xbot.socket.update

import com.xbot.socket.Client
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

@OptIn(ExperimentalSerializationApi::class)
@Serializable
@JsonClassDiscriminator("notice")
sealed class Update {
    abstract val data: UpdateData
}

@Serializable
sealed class UpdateData

inline fun <reified T : Update> Client.getUpdatesFlowOfType() =
    update.filterIsInstance<T>()
