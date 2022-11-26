package io.ashdavies.playground.events

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallTopAppBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.ashdavies.paging.LazyPagingItems
import io.ashdavies.paging.collectAsLazyPagingItems
import io.ashdavies.paging.errorMessage
import io.ashdavies.paging.isRefreshing
import io.ashdavies.paging.items
import io.ashdavies.paging.refresh
import io.ashdavies.playground.Event
import io.ashdavies.playground.EventsBottomBar
import io.ashdavies.playground.EventsRoot
import io.ashdavies.playground.android.fade
import io.ashdavies.playground.platform.PlatformSwipeRefresh

@Composable
@OptIn(ExperimentalMaterial3Api::class)
internal fun EventsScreen(child: EventsRoot.Child.Events) {
    val viewModel: EventsViewModel = rememberEventsViewModel()
    val pagingItems: LazyPagingItems<Event> = viewModel
        .pagingData
        .collectAsLazyPagingItems()

    Scaffold(
        topBar = { SmallTopAppBar(title = { Text("Events") }) },
        bottomBar = { EventsBottomBar(child) },
    ) { contentPadding ->
        PlatformSwipeRefresh(
            isRefreshing = pagingItems.isRefreshing,
            onRefresh = pagingItems::refresh,
        ) {
            if (pagingItems.errorMessage != null) {
                EventFailure(pagingItems.errorMessage)
            }

            LazyColumn(
                contentPadding = contentPadding,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 12.dp),
            ) {
                items(pagingItems) {
                    EventSection(it) {
                        println("Clicked ${it?.name}")
                    }
                }
            }
        }
    }
}

@Composable
private fun EventSection(event: Event?, onClick: () -> Unit) {
    Box(Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = onClick,
        ) {
            Row {
                Icon(
                    imageVector = Icons.Filled.DateRange,
                    contentDescription = null,
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .fillMaxHeight(),
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 12.dp),
                ) {
                    PlaceholderText(event?.name, style = MaterialTheme.typography.labelLarge)
                    PlaceholderText(event?.location, style = MaterialTheme.typography.labelMedium)
                    PlaceholderText(event?.dateStart, style = MaterialTheme.typography.labelSmall)
                }
            }
        }
    }
}

@Composable
private fun ColumnScope.PlaceholderText(
    text: String?,
    modifier: Modifier = Modifier,
    style: TextStyle = LocalTextStyle.current,
) {
    Text(
        overflow = TextOverflow.Ellipsis,
        text = text ?: emptyString(),
        style = style,
        maxLines = 1,
        modifier = modifier
            .padding(bottom = 2.dp)
            .defaultMinSize(minWidth = Dp(style.fontSize.value * 12))
            .align(Alignment.Start)
            .fade(text == null),
    )
}

@Composable
private fun EventFailure(message: String?, modifier: Modifier = Modifier) {
    Column(verticalArrangement = Arrangement.Center) {
        Row(horizontalArrangement = Arrangement.Center) {
            Text(
                modifier = modifier.padding(16.dp, 12.dp),
                text = message ?: "Something went wrong",
            )
        }
    }
}

@Suppress("NOTHING_TO_INLINE")
private inline fun emptyString(): String = ""
