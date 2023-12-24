package io.ashdavies.gallery

import io.ashdavies.content.PlatformContext
import io.ashdavies.util.randomUuid
import kotlinx.coroutines.withContext
import java.io.File
import kotlin.coroutines.CoroutineContext

internal actual fun StorageManager(
    pathProvider: PathProvider,
    coroutineContext: CoroutineContext,
): StorageManager = object : StorageManager {

    override suspend fun create(platformContext: PlatformContext): File = withContext(coroutineContext) {
        File(pathProvider.getImagesPath(), "${randomUuid()}.jpg").apply {
            if (!createNewFile()) throw IllegalStateException()
        }
    }

    override suspend fun delete(file: File): Boolean = withContext(coroutineContext) {
        file.delete()
    }
}
