package com.stevenpopovich.talktothat.usbinterfacing

import android.hardware.usb.UsbManager
import com.felhr.usbserial.UsbSerialDevice
import com.stevenpopovich.talktothat.MainDependencyModule

class SerialPortInterfaceBuilder {
    fun getSerialPortInterface(
        serialPortReader: SerialPortReader = MainDependencyModule.serialPortReader,
        usbManager: UsbManager = MainDependencyModule.usbManager,
        arduinoInterface: ArduinoInterface = MainDependencyModule.arduinoInterface
    ): SerialPortInterface? {
        arduinoInterface.getDevice(usbManager)?.let {
            val usbDeviceConnection = usbManager.openDevice(it)
            val serialDevice = UsbSerialDevice.createUsbSerialDevice(it, usbDeviceConnection)
            MainDependencyModule.serialDevice = serialDevice
            return SerialPortInterface(serialPortReader, serialDevice)
        }

        return null
    }
}
