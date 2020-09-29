package com.stevenpopovich.talktothat

import android.hardware.usb.UsbDevice
import android.hardware.usb.UsbDeviceConnection
import android.hardware.usb.UsbManager
import com.felhr.usbserial.UsbSerialDevice
import com.stevenpopovich.talktothat.testutils.relaxedMock
import com.stevenpopovich.talktothat.testutils.verifyExactlyOne
import io.mockk.every
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.HashMap

class ArduinoInterfaceTest {
    private val arduinoInterface = ArduinoInterface()

    @Test
    fun testArduinoConstants() {
        assertEquals(67, arduinoInterface.PRODUCT_ID)
        assertEquals(9025, arduinoInterface.VENDOR_ID)
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

        arduinoInterface.writeStringToSerialPort(usbManager, arbitraryString, device, serialPortWriter)

        verifyExactlyOne { serialPortWriter.createConnection(usbManager, device) }
        verifyExactlyOne { serialPortWriter.createSerialPort(device, connection) }
        verifyExactlyOne { serialPortWriter.writeToSerialPort(serialPort, arbitraryString) }
    }

    @Test
    fun testGetDevice() {
        val usbManager: UsbManager = relaxedMock()
        val entry: MutableMap.MutableEntry<String, UsbDevice> = relaxedMock()
        val usbDevice: UsbDevice = relaxedMock()
        val map: HashMap<String, UsbDevice> = relaxedMock()

        every { map.entries } returns mutableSetOf(entry)
        every { usbManager.deviceList } returns map

        every { entry.key } returns "arbitrary"
        every { entry.value } returns usbDevice

        every { usbDevice.vendorId } returns ArduinoInterface().VENDOR_ID
        every { usbDevice.productId } returns ArduinoInterface().PRODUCT_ID

        val actualDevice = arduinoInterface.getDevice(usbManager)

        assertEquals(usbDevice, actualDevice)
    }
}
