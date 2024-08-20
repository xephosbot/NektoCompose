package com.xbot.socket.update

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("unknown")
data class UpdateUnknown(override val data: UnknownData) : Update()

@Serializable
data class UnknownData(
    val description: String
) : UpdateData()
