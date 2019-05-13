package com.github.eunsiljo.coroutine.coroutine

import com.github.eunsiljo.coroutine.BaseViewModel
import kotlinx.coroutines.*

class CoroutineViewModel : BaseViewModel() {
    companion object {
        private const val COROUTINE_RESULT = "COROUTINE RESULT!"
    }

    private val coroutineScope by lazy { CoroutineScope(Dispatchers.Main) }

    fun getCoroutineResult(sleepMillis: Long) =
        coroutineScope.launch {
            setLoadingValue(true)

            try {
                setResultValue(sleep(sleepMillis).await())
            } catch (exception: Exception) {
                setErrorValue(exception)
            } finally {
                setLoadingValue(false)
            }
        }

    private suspend fun sleep(sleepMillis: Long): Deferred<String> =
        CoroutineScope(Dispatchers.IO).async {
            Thread.sleep(sleepMillis)
            COROUTINE_RESULT
        }

    override fun onCleared() {
        super.onCleared()
        coroutineScope.coroutineContext.cancel()
    }
}