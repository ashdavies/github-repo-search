package io.ashdavies.gallery

import io.ashdavies.playground.mapToList
import kotlinx.coroutines.flow.Flow

internal interface ImageManager {
    fun list(): Flow<List<Image>>
    fun add(file: File)
    fun remove(image: Image)
}

internal fun ImageManager(
    storage: StorageManager,
    queries: ImageQueries,
): ImageManager = object : ImageManager {

    override fun list(): Flow<List<Image>> {
        return queries.selectAll().mapToList()
    }

    override fun add(file: File) {
        val image = Image(
            uuid = randomUuid(),
            name = file.getName(),
            path = file.getAbsolutePath(),
        )

        queries.insertOrReplace(image)
    }

    override fun remove(image: Image) {
        storage.delete(File(image.path))
        queries.deleteById(image.uuid)
    }
}
