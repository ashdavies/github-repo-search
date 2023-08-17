package io.ashdavies.http

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

@PublishedApi
internal class LoadingException(val progress: Float) : Exception()

public val Result<*>.isLoading: Boolean
    get() = exceptionOrNull() is LoadingException

public fun <T> Result.Companion.loading(progress: Float = 0f): Result<T> = failure(LoadingException(progress))

@OptIn(ExperimentalContracts::class)
public inline fun <T> Result<T>.onLoading(action: (progress: Float) -> Unit): Result<T> {
    contract { callsInPlace(action, InvocationKind.AT_MOST_ONCE) }
    if (isLoading) action((exceptionOrNull() as LoadingException).progress)
    return this
}
