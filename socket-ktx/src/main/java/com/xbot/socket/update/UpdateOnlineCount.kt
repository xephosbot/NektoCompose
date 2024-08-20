package com.xbot.socket.update

import com.xbot.socket.Client
import kotlinx.coroutines.flow.map
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

fun Client.onlineCountFlow() = getUpdatesFlowOfType<UpdateOnlineCount>().map { it.data }

@Serializable
@SerialName("online.count")
data class UpdateOnlineCount(override val data: OnlineCountData) : Update()

@Serializable
data class OnlineCountData(
    val inChats: Int,
    val inSearch: Int,
    val inServer: Int
) : UpdateData()
