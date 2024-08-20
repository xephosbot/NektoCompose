package com.xbot.socket.update

import com.xbot.socket.Client
import kotlinx.coroutines.flow.map
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

fun Client.errorFlow() = getUpdatesFlowOfType<UpdateError>().map { it.data }

@Serializable
@SerialName("error.code")
data class UpdateError(override val data: ErrorData) : Update()

@Serializable
data class ErrorData(
    val id: Int,
    val description: String
) : UpdateData()
