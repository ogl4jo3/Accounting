package com.ogl4jo3.accounting.common

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.plus
import timber.log.Timber


fun <T> Flow<T>.launchInWithDefaultErrorHandler(
    scope: CoroutineScope,
    onError: (exceptionMsg: String) -> Unit = {},
) =
    catch { exception ->
        Timber.e(
            "launchInWithDefaultErrorHandler caught $exception with suppressed ${(exception.suppressed ?: emptyArray()).contentToString()}",
        )
        onError("${exception.message}")
    }.launchIn(scope + defaultMessageCoroutineExceptionHandler { _, exceptionMsg ->
        onError(exceptionMsg)
    })