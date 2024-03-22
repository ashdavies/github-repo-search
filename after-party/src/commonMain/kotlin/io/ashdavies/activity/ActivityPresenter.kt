package io.ashdavies.activity

import androidx.compose.runtime.Composable
import androidx.compose.runtime.RememberObserver
import androidx.compose.runtime.Stable
import app.cash.paging.ExperimentalPagingApi
import app.cash.paging.Pager
import app.cash.paging.cachedIn
import app.cash.paging.compose.collectAsLazyPagingItems
import com.slack.circuit.retained.rememberRetained
import io.ashdavies.events.Event
import io.ashdavies.events.rememberEventPager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlin.coroutines.CoroutineContext

private const val COROUTINE_SCOPE = "COROUTINE_SCOPE"

@Composable
@OptIn(ExperimentalPagingApi::class)
internal fun ActivityPresenter(
    coroutineScope: CoroutineScope = rememberRetainedCoroutineScope(),
    eventPager: Pager<String, Event> = rememberEventPager(),
): ActivityScreen.State {
    val pagingData = rememberRetained(coroutineScope) {
        eventPager.flow.cachedIn(coroutineScope)
    }

    return ActivityScreen.State(
        pagingItems = pagingData.collectAsLazyPagingItems(),
    )
}

@Stable
private class StableCoroutineScope(scope: CoroutineScope) : CoroutineScope by scope

@Composable
private fun rememberRetainedCoroutineScope(
    context: CoroutineContext = Dispatchers.Main.immediate,
): StableCoroutineScope = rememberRetained(COROUTINE_SCOPE) {
    val coroutineScope = StableCoroutineScope(CoroutineScope(context + Job()))
    rememberObserver(coroutineScope::cancel)
    coroutineScope
}

private fun rememberObserver(onForgotten: () -> Unit) = object : RememberObserver {
    override fun onAbandoned() = onForgotten()
    override fun onForgotten() = onForgotten()
    override fun onRemembered() = Unit
}
