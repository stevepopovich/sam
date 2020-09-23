package com.stevenpopovich.talktothat

import android.hardware.usb.UsbDevice
import android.hardware.usb.UsbManager

class ArduinoInterface : USBInterface {
    override val PRODUCT_ID = 67
    override val VENDOR_ID = 9025

    fun writeStringToSerialPort(usbManager: UsbManager, stringToSend: String, arduinoDevice: UsbDevice, serialPortWriter: SerialPortWriter) {
        val connection = serialPortWriter.createConnection(usbManager, arduinoDevice)
        val serialPort = serialPortWriter.createSerialPort(arduinoDevice, connection)
        serialPortWriter.writeToSerialPort(serialPort, stringToSend)
    }
}
