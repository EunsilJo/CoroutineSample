package com.github.eunsiljo.coroutine.rx

import androidx.lifecycle.MutableLiveData
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class RxViewModel {
    private val compositeDisposable by lazy { CompositeDisposable() }

    val resultState by lazy { MutableLiveData<String>() }
    val loadingState by lazy { MutableLiveData<Boolean>() }
    val errorState by lazy { MutableLiveData<Throwable>() }

    fun getRxResult(sleepMillis: Long) {
        Single.create(SleepOnSubscribe(sleepMillis))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { loadingState.value = true }
            .doFinally { loadingState.value = false }
            .subscribe(
                { result -> resultState.value = result },
                { throwable -> errorState.value = throwable }
            )
            .also { compositeDisposable.add(it) }
    }

    fun onDestroy() {
        compositeDisposable.dispose()
    }
}