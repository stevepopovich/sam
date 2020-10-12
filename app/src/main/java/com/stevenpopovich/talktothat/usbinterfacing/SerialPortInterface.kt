package com.stevenpopovich.talktothat.usbinterfacing

import com.felhr.usbserial.UsbSerialDevice
import com.felhr.usbserial.UsbSerialInterface

class SerialPortInterface(
    serialPortReader: SerialPortReader,
    private val serialDevice: UsbSerialDevice
) {
    init {
        serialDevice.open()
        serialDevice.setBaudRate(9600)
        serialDevice.setDataBits(UsbSerialInterface.DATA_BITS_8)
        serialDevice.setStopBits(UsbSerialInterface.STOP_BITS_1)
        serialDevice.setParity(UsbSerialInterface.PARITY_NONE)
        serialDevice.setFlowControl(UsbSerialInterface.FLOW_CONTROL_OFF)
        serialDevice.read(serialPortReader)
    }

    fun writeToSerialPort(stringToSend: String) {
        serialDevice.write(stringToSend.toByteArray())
    }
}
