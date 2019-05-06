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
            try {
                setLoadingValue(true)

                CoroutineScope(Dispatchers.IO).launch {
                    Thread.sleep(sleepMillis)

                    CoroutineScope(Dispatchers.Main).launch {
                        setLoadingValue(false)
                        setResultValue(COROUTINE_RESULT)
                    }
                }
            } catch (exception: Exception) {
                setErrorValue(exception)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        coroutineScope.coroutineContext.cancel()
    }
}