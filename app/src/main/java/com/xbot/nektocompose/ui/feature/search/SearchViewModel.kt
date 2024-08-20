package com.xbot.nektocompose.ui.feature.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xbot.domain.model.ConnectionState
import com.xbot.domain.repository.AuthRepository
import com.xbot.nektocompose.R
import com.xbot.ui.component.Message
import com.xbot.ui.component.MessageContent
import com.xbot.ui.component.SnackbarManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class TestViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val snackbarManager: SnackbarManager
) : ViewModel() {

    val state: StateFlow<SearchScreenState> = combine(
        authRepository.connectionState,
        authRepository.onlineCount
    ) { connectionState, onlineCount ->
        when (connectionState) {
            is ConnectionState.Connected -> SearchScreenState.Ready(
                onlineCount = onlineCount,
                inSearchCount = 0
            )
            else -> SearchScreenState.Loading
        }
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = SearchScreenState.Loading
        )

    init {
        viewModelScope.launch {
            authRepository.auth()

            authRepository.connectionState.onEach { state ->
                when (state) {
                    is ConnectionState.Connected -> {
                        authRepository.auth()
                        showSnackbar(R.string.snackbar_connect)
                    }
                    is ConnectionState.Disconnected -> {
                        showSnackbar(R.string.snackbar_disconnect)
                    }
                    is ConnectionState.Error -> {
                        showSnackbar(R.string.snackbar_connect_error)
                    }
                }
            }.collect()
        }
    }

    fun onAction(action: SearchScreenAction) {
        //TODO:
    }

    private fun showSnackbar(textId: Int) {
        snackbarManager.showMessage(
            Message(
                id = UUID.randomUUID().mostSignificantBits,
                title = MessageContent.Text(textId)
            )
        )
    }
}


