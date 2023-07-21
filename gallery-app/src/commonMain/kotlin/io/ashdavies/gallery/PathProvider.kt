package io.ashdavies.gallery

import androidx.compose.runtime.Composable

internal interface PathProvider {
    val images: File
}

internal expect fun PathProvider(
    context: Context,
): PathProvider

@Composable
internal expect fun rememberPathProvider(): PathProvider