package com.xbot.domain.repository

import com.xbot.domain.model.ConnectionState
import kotlinx.coroutines.flow.Flow

interface AuthRepository {

    suspend fun auth()

    val connectionState: Flow<ConnectionState>

    val onlineCount: Flow<Int>
}