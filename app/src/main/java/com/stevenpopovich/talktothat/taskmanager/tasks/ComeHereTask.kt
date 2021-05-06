package com.stevenpopovich.talktothat.taskmanager.tasks

import com.stevenpopovich.talktothat.MainDependencyModule
import com.stevenpopovich.talktothat.usbinterfacing.ArduinoInterface
import com.stevenpopovich.talktothat.usbinterfacing.SerialPortInterface

class ComeHereTask(
    private val arduinoInterface: ArduinoInterface = MainDependencyModule.arduinoInterface,
    private val serialPortInterface: SerialPortInterface? = MainDependencyModule.serialPortInterface,
) : Task {
    override fun start() {
        serialPortInterface?.let {
            arduinoInterface.writeStringToSerialPort(it, "forward")
        }
    }

    override fun finish() {
        super.finish()
        serialPortInterface?.let {
            arduinoInterface.writeStringToSerialPort(it, "stop")
        }
    }
}
