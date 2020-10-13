package com.stevenpopovich.talktothat.usbinterfacing

import android.hardware.usb.UsbManager
import com.felhr.usbserial.UsbSerialDevice

class SerialPortInterfaceBuilder {
    fun getSerialPortInterface(
        serialPortReader: SerialPortReader,
        usbManager: UsbManager,
        arduinoInterface: ArduinoInterface
    ): SerialPortInterface? {
        arduinoInterface.getDevice(usbManager)?.let {
            val usbDeviceConnection = usbManager.openDevice(it)
            val serialDevice = UsbSerialDevice.createUsbSerialDevice(it, usbDeviceConnection)
            return SerialPortInterface(serialPortReader, serialDevice)
        }

        return null
    }
}
