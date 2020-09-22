package com.stevenpopovich.talktothat

import android.hardware.usb.UsbDevice
import android.hardware.usb.UsbDeviceConnection
import android.hardware.usb.UsbManager
import com.felhr.usbserial.UsbSerialDevice
import com.felhr.usbserial.UsbSerialInterface

class SerialPortWriter {
    fun createConnection(usbManager: UsbManager, device: UsbDevice): UsbDeviceConnection =
        usbManager.openDevice(device)

    fun createSerialPort(device: UsbDevice, connection: UsbDeviceConnection): UsbSerialDevice =
        UsbSerialDevice.createUsbSerialDevice(device, connection)

    fun writeToSerialPort(serialPort: UsbSerialDevice, stringToSend: String) {
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
