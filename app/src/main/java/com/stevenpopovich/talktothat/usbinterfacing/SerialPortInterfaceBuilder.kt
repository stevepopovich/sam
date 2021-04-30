package com.stevenpopovich.talktothat.usbinterfacing

import android.hardware.usb.UsbManager
import com.felhr.usbserial.UsbSerialDevice
import com.stevenpopovich.talktothat.MainDependencyModule

fun getSerialPortInterface(
    usbManager: UsbManager = MainDependencyModule.usbManager,
    arduinoInterface: ArduinoInterface = MainDependencyModule.arduinoInterface
): SerialPortInterface? {
    return arduinoInterface.getDevice(usbManager)?.let {
        val usbDeviceConnection = usbManager.openDevice(it)
        val serialDevice = UsbSerialDevice.createUsbSerialDevice(it, usbDeviceConnection)
        return SerialPortInterface(serialDevice)
    }
}
