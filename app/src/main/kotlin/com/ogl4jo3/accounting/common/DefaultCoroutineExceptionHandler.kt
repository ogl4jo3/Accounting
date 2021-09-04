package com.ogl4jo3.accounting.common

import kotlinx.coroutines.CoroutineExceptionHandler
import timber.log.Timber

fun defaultMessageCoroutineExceptionHandler(
    onError: (code: Int, message: String) -> Unit,
): CoroutineExceptionHandler =
    CoroutineExceptionHandler { coroutineContext, exception ->
        defaultCoroutineExceptionHandler.handleException(coroutineContext, exception)
        onError(exception.hashCode(), "${exception.message}")
    }

val defaultCoroutineExceptionHandler =
    CoroutineExceptionHandler { _, exception ->
        Timber.e(
            "CoroutineExceptionHandler caught $exception with suppressed ${(exception.suppressed ?: emptyArray()).contentToString()}",
        )
    }
