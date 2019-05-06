package com.github.eunsiljo.coroutine

import androidx.lifecycle.MutableLiveData

open class BaseViewModel {
    private val resultState: MutableLiveData<String> = MutableLiveData()
    private val loadingState: MutableLiveData<Boolean> = MutableLiveData()
    private val errorState: MutableLiveData<Throwable?> = MutableLiveData()

    protected fun setResultValue(value: String) {
        resultState.value = value
    }
    protected fun setLoadingValue(value: Boolean) {
        loadingState.value = value
    }
    protected fun setErrorValue(value: Throwable?) {
        errorState.value = value
    }

    fun getResultState() = resultState
    fun getLoadingState() = loadingState
    fun getErrorState() = errorState
}