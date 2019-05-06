package com.github.eunsiljo.coroutine.rx

import io.reactivex.SingleEmitter
import io.reactivex.SingleOnSubscribe
import java.lang.Exception

class SleepOnSubscribe(private val sleepMillis: Long) : SingleOnSubscribe<String> {
    companion object {
        private const val RX_RESULT = "RX RESULT!"
    }

    override fun subscribe(emitter: SingleEmitter<String>) {
        if (!emitter.isDisposed) {
            try {
                Thread.sleep(sleepMillis)
            } catch (exception: Exception) {
                emitter.onError(exception)
            }
            emitter.onSuccess(RX_RESULT)
        }
    }
}