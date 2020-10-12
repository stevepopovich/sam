package com.stevenpopovich.talktothat.usbinterfacing

import android.hardware.usb.UsbManager
import com.felhr.usbserial.UsbSerialDevice

class ArduinoInterface(
    private val serialPortReader: SerialPortReader,
    private val usbManager: UsbManager
) : USBInterface {
    override val PRODUCT_ID = 67
    override val VENDOR_ID = 9025

    private var serialPortInterface: SerialPortInterface? = null

    init {
        this.getDevice(usbManager)?.let {
            val usbDeviceConnection = usbManager.openDevice(it)
            val serialDevice = UsbSerialDevice.createUsbSerialDevice(it, usbDeviceConnection)
            serialPortInterface = SerialPortInterface(serialPortReader, serialDevice)
        }
    }

    fun writeStringToSerialPort(stringToSend: String) {
        serialPortInterface?.writeToSerialPort(stringToSend + "\n")
    }
}
