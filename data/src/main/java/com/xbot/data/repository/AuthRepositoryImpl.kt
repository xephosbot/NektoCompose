package com.xbot.data.repository

import com.xbot.data.mapToDomainModel
import com.xbot.data.source.TokenDataSource
import com.xbot.domain.model.ConnectionState
import com.xbot.domain.repository.AuthRepository
import com.xbot.socket.Client
import com.xbot.socket.connectionStateFlow
import com.xbot.socket.request.authGetToken
import com.xbot.socket.request.authSendToken
import com.xbot.socket.request.onlineTrack
import com.xbot.socket.update.authSuccessTokenFlow
import com.xbot.socket.update.onlineCountFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val client: Client,
    private val tokenDataSource: TokenDataSource,
    private val provider: DeviceInfoProvider
) : AuthRepository {

    override suspend fun auth() {
        client.connect()
        handleAuthToken()
        client.onlineTrack(true)
    }

    private suspend fun handleAuthToken() {
        tokenDataSource.authToken.firstOrNull()?.let { authToken ->
            client.authSendToken(
                token = authToken,
                pushToken = PUSH_TOKEN,
                deviceId = provider.deviceId
            )
        } ?: run {
            client.authGetToken(
                deviceType = provider.deviceType,
                deviceId = provider.deviceId,
                pushToken = PUSH_TOKEN,
                deviceName = provider.deviceName
            )
            client.authSuccessTokenFlow().first().let { data ->
                tokenDataSource.setAuthToken(data.tokenInfo.authToken)
            }
        }
    }

    override val connectionState: Flow<ConnectionState> = client.connectionStateFlow()
        .onEach { println(it) } //TODO: Clean up
        .map { state -> state.mapToDomainModel() }

    override val onlineCount: Flow<Int> = client.onlineCountFlow()
        .onEach { println(it) } //TODO: Clean up
        .map { it.inChats }

    companion object {
        private const val PUSH_TOKEN: String = "dslkj34-sdflkajsf43"
    }
}