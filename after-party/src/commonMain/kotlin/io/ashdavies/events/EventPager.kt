package io.ashdavies.events

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import io.ashdavies.http.LocalHttpClient
import io.ashdavies.network.todayAsString
import io.ashdavies.sql.rememberLocalQueries

private const val DEFAULT_PAGE_SIZE = 10

@Composable
@ExperimentalPagingApi
internal fun rememberEventPager(
    eventsQueries: EventsQueries = rememberLocalQueries { it.eventsQueries },
    eventsCallable: GetEventsCallable = GetEventsCallable(LocalHttpClient.current),
    initialKey: String = todayAsString(),
    pageSize: Int = DEFAULT_PAGE_SIZE,
): Pager<String, Event> = remember(eventsQueries, eventsCallable) {
    Pager(
        config = PagingConfig(pageSize),
        initialKey = initialKey,
        remoteMediator = EventsRemoteMediator(eventsQueries, eventsCallable),
        pagingSourceFactory = { EventsPagingSource(eventsQueries) },
    )
}
