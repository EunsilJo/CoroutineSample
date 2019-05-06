package com.github.eunsiljo.coroutine

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import com.github.eunsiljo.coroutine.rx.RxViewModel
import com.github.eunsiljo.coroutine.thread.ThreadViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    companion object {
        private const val SLEEP_MILLIS = 5000L
    }

    private val threadViewModel by lazy { ThreadViewModel() }

    private val rxViewModel by lazy { RxViewModel() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        subscribeViewModel(threadViewModel)
        subscribeViewModel(rxViewModel)

        button.setOnClickListener {
            threadViewModel.getThreadResult(SLEEP_MILLIS)
            //rxViewModel.getRxResult(SLEEP_MILLIS)
        }
    }

    override fun onDestroy() {
        rxViewModel.onDestroy()
        super.onDestroy()
    }

    private fun subscribeViewModel(viewModel: BaseViewModel) {
        viewModel.run {
            getLoadingState().observe(
                this@MainActivity,
                Observer { visible ->
                    progress.visibility = when (visible) {
                        true -> View.VISIBLE
                        false -> View.INVISIBLE
                    }
                }
            )
            getErrorState().observe(
                this@MainActivity,
                Observer { throwable ->
                    throwable?.printStackTrace()
                }
            )
            getResultState().observe(
                this@MainActivity,
                Observer { result ->
                    Toast.makeText(this@MainActivity, result, Toast.LENGTH_SHORT).show()
                }
            )
        }
    }
}
