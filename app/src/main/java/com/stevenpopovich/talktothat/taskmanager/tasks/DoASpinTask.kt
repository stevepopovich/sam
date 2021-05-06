package com.stevenpopovich.talktothat.taskmanager.tasks

import com.stevenpopovich.talktothat.MainDependencyModule
import com.stevenpopovich.talktothat.usbinterfacing.ArduinoInterface
import com.stevenpopovich.talktothat.usbinterfacing.SerialPortInterface
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import java.util.concurrent.TimeUnit

class DoASpinTask(
    private val arduinoInterface: ArduinoInterface = MainDependencyModule.arduinoInterface,
    private val serialPortInterface: SerialPortInterface? = MainDependencyModule.serialPortInterface,
) : Task {
    private val compositeDisposable = CompositeDisposable()

    override fun start() {
        serialPortInterface?.let {
            arduinoInterface.writeStringToSerialPort(it, "-50")
        }

        Single.timer(250, TimeUnit.MILLISECONDS).subscribe { _ ->
            serialPortInterface?.let {
                arduinoInterface.writeStringToSerialPort(it, "225")
            }

            Single.timer(1015, TimeUnit.MILLISECONDS).subscribe { _ -> finish() }.addTo(compositeDisposable)
        }.addTo(compositeDisposable)
    }

    override fun finish() {
        super.finish()
        serialPortInterface?.let {
            arduinoInterface.writeStringToSerialPort(it, "stop")
        }
    }
}
