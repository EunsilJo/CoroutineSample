package com.github.eunsiljo.coroutine.rx

import com.github.eunsiljo.coroutine.BaseViewModel
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class RxViewModel : BaseViewModel() {

    private val compositeDisposable by lazy { CompositeDisposable() }

    fun getRxResult(sleepMillis: Long) {
        Single.create(SleepOnSubscribe(sleepMillis))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { setLoadingValue(true) }
            .doFinally { setLoadingValue(false) }
            .subscribe(
                { result -> setResultValue(result) },
                { throwable -> setErrorValue(throwable) }
            )
            .also { compositeDisposable.add(it) }
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}