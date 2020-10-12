package com.stevenpopovich.talktothat.usbinterfacing

import android.widget.TextView
import com.felhr.usbserial.UsbSerialInterface
import com.stevenpopovich.talktothat.MainFragment
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.subjects.BehaviorSubject
import java.util.concurrent.TimeUnit

class SerialPortReader(
    private val debugTextView: TextView,
    private val mainFragment: MainFragment
) : UsbSerialInterface.UsbReadCallback {
    private val compositeDisposable = CompositeDisposable()

    private val publishSubject: BehaviorSubject<ByteArray> = BehaviorSubject.create()

    private var currentBytesToPrint: ByteArray = ByteArray(0)

    init {
        publishSubject
            .doOnNext { currentBytesToPrint += it }
            .debounce(300, TimeUnit.MILLISECONDS)
            .subscribe { _ ->
                val stringToOutput = currentBytesToPrint.toString(Charsets.UTF_8)
                if (stringToOutput.isNotBlank()) {
                    mainFragment.activity?.runOnUiThread {
                        debugTextView.text = stringToOutput
                    }
                }
                currentBytesToPrint = ByteArray(0)
            }
            .addTo(compositeDisposable)
    }

    override fun onReceivedData(data: ByteArray?) {
        publishSubject.onNext(data)
    }
}
