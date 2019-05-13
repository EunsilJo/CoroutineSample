package com.github.eunsiljo.coroutine.coroutine

import com.github.eunsiljo.coroutine.BaseViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class CoroutineViewModel : BaseViewModel() {
    companion object {
        private const val COROUTINE_RESULT = "COROUTINE RESULT!"
    }

    private val coroutineScope by lazy { CoroutineScope(Dispatchers.Main) }

    fun getCoroutineResult(sleepMillis: Long) {
        coroutineScope.launch {
            setLoadingValue(true)

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    Thread.sleep(sleepMillis)

                    CoroutineScope(Dispatchers.Main).launch {
                        setResultValue(COROUTINE_RESULT)
                    }
                } catch (exception: Exception) {
                    CoroutineScope(Dispatchers.Main).launch {
                        setErrorValue(exception)
                    }
                } finally {
                    CoroutineScope(Dispatchers.Main).launch {
                        setLoadingValue(false)
                    }
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        coroutineScope.coroutineContext.cancel()
    }
}