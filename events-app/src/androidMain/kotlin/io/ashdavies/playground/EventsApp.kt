package io.ashdavies.playground

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.ComponentContext

@Composable
internal fun EventsApp(componentContext: ComponentContext) {
    ComposeScreen { EventsRoot(componentContext) }
}
