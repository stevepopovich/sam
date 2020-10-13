package com.stevenpopovich.talktothat.usbinterfacing

class ArduinoInterface : USBInterface {
    override val PRODUCT_ID = 67
    override val VENDOR_ID = 9025

    fun writeStringToSerialPort(serialPortInterface: SerialPortInterface, stringToSend: String) {
        serialPortInterface.writeToSerialPort(stringToSend + "\n")
    }
}
