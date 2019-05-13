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
                withContext(Dispatchers.IO) {
                    Thread.sleep(sleepMillis)
                }

                setResultValue(COROUTINE_RESULT)
            } catch (exception: Exception) {
                setErrorValue(exception)
            } finally {
                setLoadingValue(false)
            }
        }

    override fun onCleared() {
        super.onCleared()
        coroutineScope.coroutineContext.cancel()
    }
}