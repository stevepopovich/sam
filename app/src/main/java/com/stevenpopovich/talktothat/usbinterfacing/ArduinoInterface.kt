package com.stevenpopovich.talktothat.usbinterfacing

import com.stevenpopovich.talktothat.verboseLog

class ArduinoInterface : USBInterface {
    override val PRODUCT_ID = 67
    override val VENDOR_ID = 9025

    fun writeStringToSerialPort(serialPortInterface: SerialPortInterface, stringToSend: String) {
        stringToSend.verboseLog()
        serialPortInterface.writeToSerialPort(stringToSend + "\n")
    }
}
