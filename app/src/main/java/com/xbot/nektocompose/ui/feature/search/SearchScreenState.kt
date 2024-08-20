package com.xbot.nektocompose.ui.feature.search

sealed interface SearchScreenState {
    data object Loading : SearchScreenState
    data class Ready(
        val onlineCount: Int = 0,
        val inSearchCount: Int = 0
    ) : SearchScreenState
}

sealed interface SearchScreenAction {
    data object Search : SearchScreenAction
}
