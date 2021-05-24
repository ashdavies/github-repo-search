package io.ashdavies.playground.service

import io.ashdavies.playground.store.Cache
import io.ashdavies.playground.store.Options
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimePeriod
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone.Companion.UTC
import kotlinx.datetime.periodUntil

private val now: Instant
    get() = Clock.System.now()

internal interface CachePolicy {

    suspend fun isStale(forceUpdate: Boolean): Boolean
    suspend fun isUpToDate(value: Boolean)
}

internal fun CachePolicy(): CachePolicy {
    return ConfigurationCachePolicy(ConfigurationCache())
}

private class ConfigurationCachePolicy(
    private val cache: Cache<Configuration.Type, Configuration>,
) : CachePolicy {

    private suspend fun getUpdatedAt(): Instant? {
        return cache
            .read(Configuration.Type, Options())
            ?.updatedAt
            ?.let { Instant.parse(it) }
    }

    override suspend fun isStale(forceUpdate: Boolean): Boolean {
        val updatedAt: Instant = getUpdatedAt() ?: Instant.DISTANT_PAST
        val periodUntilNow: DateTimePeriod = updatedAt.periodUntil(now, UTC)

        return when (forceUpdate) {
            true -> periodUntilNow.hours > 2
            false -> periodUntilNow.days > 1
        }
    }

    override suspend fun isUpToDate(value: Boolean) {
        val updatedAt: Instant = if (value) now else Instant.DISTANT_PAST
        cache.write(Configuration.Type, Configuration(updatedAt.toString()))
    }
}