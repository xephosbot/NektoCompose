package com.xbot.nektocompose.ui.feature.search

import androidx.compose.animation.Crossfade
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.PrimaryIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.xbot.nektocompose.R
import com.xbot.ui.component.AnimatedFloatingActionButton
import com.xbot.ui.component.Scaffold
import com.xbot.ui.component.Tab
import com.xbot.ui.component.pagerTabIndicatorOffset
import com.xbot.ui.icon.Icons
import kotlinx.coroutines.launch

@Composable
fun SearchScreen(
    viewModel: TestViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    SearchScreenContent(
        state = state,
        onAction = viewModel::onAction
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
private fun SearchScreenContent(
    modifier: Modifier = Modifier,
    state: SearchScreenState,
    onAction: (SearchScreenAction) -> Unit
) {
    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState { SearchTabs.entries.size }
    val selectedTabIndex by remember { derivedStateOf { pagerState.currentPage } }

    Scaffold(
        modifier = modifier,
        topBar = {
            Column {
                TopAppBar(
                    title = {
                        Column {
                            Text(
                                text = stringResource(id = R.string.app_name),
                                fontSize = 20.sp
                            )
                            val subtitle = when (state) {
                                is SearchScreenState.Loading -> "Соединение..."
                                is SearchScreenState.Ready -> "Онлайн: ${state.onlineCount}"
                            }
                            Text(
                                text = subtitle,
                                fontSize = 12.sp
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(
                                imageVector = Icons.Settings,
                                contentDescription = "Settings"
                            )
                        }
                    }
                )
                TabRow(
                    selectedTabIndex = selectedTabIndex,
                    divider = {},
                    indicator = { tabPositions ->
                        if (selectedTabIndex < tabPositions.size) {
                            PrimaryIndicator(
                                modifier = Modifier.pagerTabIndicatorOffset(pagerState, tabPositions),
                                width = Dp.Unspecified
                            )
                        }
                    }
                ) {
                    SearchTabs.entries.forEachIndexed { index, tabItem ->
                        Tab(
                            selected = index == selectedTabIndex,
                            onClick = {
                                scope.launch {
                                    pagerState.animateScrollToPage(tabItem.ordinal)
                                }
                            },
                            text = {
                                Text(
                                    text = tabItem.title,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        )
                    }
                }
            }
        },
        floatingActionButton = {
            AnimatedFloatingActionButton(
                onClick = { /*TODO*/ },
                visible = state is SearchScreenState.Ready
            ) {
                Icon(
                    imageVector = Icons.Search,
                    contentDescription = ""
                )
            }
        }
    ) { contentPadding ->
        HorizontalPager(
            contentPadding = contentPadding,
            state = pagerState
        ) { index ->
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = SearchTabs.entries[index].title)
            }
        }
    }
}

@Composable
private fun SearchButton(
    modifier: Modifier = Modifier,
    enabled: Boolean,
    onClick: () -> Unit,
    content: @Composable RowScope.(Boolean) -> Unit
) {
    Crossfade(targetState = enabled, label = "") {
        Button(
            modifier = modifier.fillMaxWidth(),
            enabled = it,
            onClick = onClick,
            content = { content(!it) }
        )
    }
}

enum class SearchTabs(val title: String) {
    CHAT("Общение"),
    FLIRT("Флирт 18+"),
    ROLE("Ролка")
}