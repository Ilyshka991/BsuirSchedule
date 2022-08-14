@file:Suppress("UNCHECKED_CAST")

package com.pechuro.bsuirschedule.domain.common

class Result<out T> private constructor(val value: Any?) {

    val isSuccess: Boolean
        get() = value !is Failure
    val isFailure: Boolean
        get() = value is Failure

    fun getOrNull(): T? =
        when {
            isFailure -> null
            else -> value as T
        }

    fun exceptionOrNull(): Throwable? =
        when (value) {
            is Failure -> value.exception
            else -> null
        }

    companion object {

        fun <T> success(value: T): Result<T> = Result(value)

        fun <T> failure(exception: Throwable): Result<T> = Result(createFailure(exception))
    }

    class Failure(val exception: Throwable)
}

inline fun <R> runCatching(block: () -> R): Result<R> {
    return try {
        Result.success(block())
    } catch (e: Throwable) {
        Result.failure(e)
    }
}

inline fun <T, R> T.runCatching(block: T.() -> R): Result<R> {
    return try {
        Result.success(block())
    } catch (e: Throwable) {
        Result.failure(e)
    }
}

inline fun <R, T : R> Result<T>.getOrElse(onFailure: (exception: Throwable) -> R): R {
    return when (val exception = exceptionOrNull()) {
        null -> value as T
        else -> onFailure(exception)
    }
}

fun <R, T : R> Result<T>.getOrDefault(defaultValue: R): R {
    if (isFailure) return defaultValue
    return value as T
}

inline fun <R, T> Result<T>.fold(
    onSuccess: (value: T) -> R,
    onFailure: (exception: Throwable) -> R
): R {
    return when (val exception = exceptionOrNull()) {
        null -> onSuccess(value as T)
        else -> onFailure(exception)
    }
}

inline fun <T> Result<T>.onFailure(action: (exception: Throwable) -> Unit): Result<T> {
    exceptionOrNull()?.let { action(it) }
    return this
}

inline fun <T> Result<T>.onSuccess(action: (value: T) -> Unit): Result<T> {
    if (isSuccess) action(value as T)
    return this
}

private fun createFailure(exception: Throwable): Any =
    Result.Failure(exception)