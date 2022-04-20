package io.ashdavies.playground

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.arkivanov.decompose.ComponentContext

@Composable
internal fun PlaygroundScreen(componentContext: ComponentContext) {
    PlaygroundScreen {
        val root: PlaygroundRoot = remember(componentContext) {
            PlaygroundRootComponent(componentContext)
        }

        PlaygroundRoot(root)
    }
}
