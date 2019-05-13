package com.github.eunsiljo.coroutine

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

open class BaseViewModel : ViewModel() {

    private val _resultState: MutableLiveData<String> = MutableLiveData()
    private val _loadingState: MutableLiveData<Boolean> = MutableLiveData()
    private val _errorState: MutableLiveData<Throwable?> = MutableLiveData()

    val resultState: LiveData<String>
        get() = _resultState
    val loadingState: LiveData<Boolean>
        get() = _loadingState
    val errorState: LiveData<Throwable?>
        get() = _errorState

    fun setResultValue(value: String) {
        _resultState.value = value
    }
    fun setLoadingValue(value: Boolean) {
        _loadingState.value = value
    }
    fun setErrorValue(value: Throwable?) {
        _errorState.value = value
    }
}