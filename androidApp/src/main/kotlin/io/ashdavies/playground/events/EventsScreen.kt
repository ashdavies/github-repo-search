package io.ashdavies.playground.events

import android.content.Context
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
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.google.accompanist.insets.ui.LocalScaffoldPadding
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.squareup.sqldelight.android.AndroidSqliteDriver
import io.ashdavies.playground.DatabaseFactory
import io.ashdavies.playground.Event
import io.ashdavies.playground.EventsQueries
import io.ashdavies.playground.PlaygroundDatabase
import io.ashdavies.playground.common.viewModel
import io.ashdavies.playground.compose.fade
import io.ashdavies.playground.emptyString
import io.ashdavies.playground.network.EventsService
import io.ashdavies.playground.network.LocalHttpClient
import io.ktor.client.HttpClient
import kotlinx.coroutines.runBlocking

@Preview
@Composable
internal fun EventsScreen() {
    val httpClient: HttpClient = LocalHttpClient.current
    val eventsService: EventsService = remember {
        EventsService(httpClient)
    }

    val context: Context = LocalContext.current
    val eventsQueries: EventsQueries = remember {
        runBlocking {
            DatabaseFactory { AndroidSqliteDriver(it, context) }
                .create(PlaygroundDatabase.Schema) { PlaygroundDatabase(it) }
                .eventsQueries
        }
    }

    val viewModel: EventsViewModel = viewModel {
        EventsViewModel(eventsQueries, eventsService)
    }

    val pagingItems: LazyPagingItems<Event> = viewModel
        .pagingData
        .collectAsLazyPagingItems()

    SwipeRefresh(
        state = rememberSwipeRefreshState(pagingItems.loadState.refresh is LoadState.Loading),
        indicatorPadding = LocalScaffoldPadding.current,
        onRefresh = pagingItems::refresh,
    ) {
        val loadState = pagingItems.loadState.append
        if (loadState is LoadState.Error) {
            EventFailure(loadState.error.message)
        }

        LazyColumn(
            contentPadding = LocalScaffoldPadding.current,
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

@Composable
internal fun EventSection(event: Event?, onClick: () -> Unit) {
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
                        .fillMaxHeight()
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 12.dp)
                ) {
                    PlaceholderText(event?.name, style = MaterialTheme.typography.h6)
                    PlaceholderText(event?.location, style = MaterialTheme.typography.body1)
                    PlaceholderText(event?.dateStart, style = MaterialTheme.typography.body2)
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
internal fun EventFailure(message: String?, modifier: Modifier = Modifier) {
    Column(verticalArrangement = Arrangement.Center) {
        Row(horizontalArrangement = Arrangement.Center) {
            Text(
                modifier = modifier.padding(16.dp, 12.dp),
                text = message ?: "Something went wrong",
            )
        }
    }
}
