package com.stevenpopovich.talktothat

import android.hardware.usb.UsbDevice
import android.hardware.usb.UsbDeviceConnection
import android.hardware.usb.UsbManager
import com.felhr.usbserial.UsbSerialDevice
import io.mockk.every
import org.junit.Assert.assertEquals
import org.junit.Test

class ArduinoInterfaceTest {
    @Test
    fun testArduinoConstants() {
        assertEquals(67, ArduinoInterface.instance.PRODUCT_ID)
        assertEquals(9025, ArduinoInterface.instance.VENDOR_ID)
    }

    @Test
    fun testWriteStringToSerialPort() {
        val usbManager: UsbManager = relaxedMock()
        val arbitraryString = "my thing"
        val device: UsbDevice = relaxedMock()
        val serialPortWriter: SerialPortWriter = relaxedMock()
        val connection: UsbDeviceConnection = relaxedMock()
        val serialPort: UsbSerialDevice = relaxedMock()

        every { serialPortWriter.createConnection(usbManager, device) } returns connection
        every { serialPortWriter.createSerialPort(device, connection) } returns serialPort

        ArduinoInterface.instance.writeStringToSerialPort(usbManager, arbitraryString, device, serialPortWriter)

        verifyExactlyOne { serialPortWriter.createConnection(usbManager, device) }
        verifyExactlyOne { serialPortWriter.createSerialPort(device, connection) }
        verifyExactlyOne { serialPortWriter.writeToSerialPort(serialPort, arbitraryString) }
    }
}
