package com.xbot.socket.update

import com.xbot.socket.Client
import kotlinx.coroutines.flow.map
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

fun Client.authSuccessTokenFlow() = getUpdatesFlowOfType<UpdateAuthSuccessToken>().map { it.data }

@Serializable
@SerialName("auth.successToken")
data class UpdateAuthSuccessToken(override val data: AuthSuccessTokenData) : Update()

@Serializable
data class AuthSuccessTokenData(
    val id: Int,
    val tokenInfo: TokenInfo,
    val statusInfo: StatusInfo
) : UpdateData()

@Serializable
data class TokenInfo(
    val createTime: Long,
    val authToken: String,
    val pushToken: String
)

@Serializable
data class StatusInfo(
    val anonDialogId: Int?
)
