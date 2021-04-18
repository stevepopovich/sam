package com.stevenpopovich.talktothat.usbinterfacing

import com.felhr.usbserial.UsbSerialInterface
import com.stevenpopovich.talktothat.MainDependencyModule
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.subjects.BehaviorSubject
import java.util.concurrent.TimeUnit

class SerialPortReader(
    private val behaviorSubject: BehaviorSubject<ByteArray> = BehaviorSubject.create(),
    private val onReadFromSerialPortLogic: OnReadFromSerialPortLogic = MainDependencyModule.onReadFromSerialPortLogic,
    compositeDisposable: CompositeDisposable = CompositeDisposable(),
) : UsbSerialInterface.UsbReadCallback {
    init {
        behaviorSubject
            .doOnNext { onReadFromSerialPortLogic.bufferBytesToPrint(it) }
            .debounce(300, TimeUnit.MILLISECONDS)
            .subscribe { onReadFromSerialPortLogic.logic() }
            .addTo(compositeDisposable)
    }

    override fun onReceivedData(data: ByteArray?) {
        behaviorSubject.onNext(data)
    }
}
