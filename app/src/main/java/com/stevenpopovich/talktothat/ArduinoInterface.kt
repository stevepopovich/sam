package com.stevenpopovich.talktothat

import android.hardware.usb.UsbManager
import com.felhr.usbserial.UsbSerialDevice
import com.felhr.usbserial.UsbSerialInterface

class ArduinoInterface private constructor() : USBInterface {
    override val PRODUCT_ID = 67
    override val VENDOR_ID = 9025

    fun writeStringToSerialPort(usbManager: UsbManager, stringToSend: String) {
        val device = getDevice(usbManager)

        if (device != null) {
            val connection = usbManager.openDevice(device)
            val serialPort = UsbSerialDevice.createUsbSerialDevice(device, connection)
            serialPort.open()
            serialPort.setBaudRate(9600)
            serialPort.setDataBits(UsbSerialInterface.DATA_BITS_8)
            serialPort.setStopBits(UsbSerialInterface.STOP_BITS_1)
            serialPort.setParity(UsbSerialInterface.PARITY_NONE)
            serialPort.setFlowControl(UsbSerialInterface.FLOW_CONTROL_OFF)
            serialPort.write(stringToSend.toByteArray())
            serialPort.close()
        }
    }

    companion object {
        val instance: ArduinoInterface by lazy {
            ArduinoInterface()
        }
    }
}
