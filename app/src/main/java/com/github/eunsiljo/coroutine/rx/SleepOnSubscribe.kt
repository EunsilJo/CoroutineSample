package com.github.eunsiljo.coroutine.rx

import io.reactivex.SingleEmitter
import io.reactivex.SingleOnSubscribe

class SleepOnSubscribe(private val millis: Long) : SingleOnSubscribe<String> {
    companion object {
        private const val RX_RESULT = "RX RESULT!"
    }

    override fun subscribe(emitter: SingleEmitter<String>) {
        if (!emitter.isDisposed) {
            Thread.sleep(millis)
            emitter.onSuccess(RX_RESULT)
        }
    }
}