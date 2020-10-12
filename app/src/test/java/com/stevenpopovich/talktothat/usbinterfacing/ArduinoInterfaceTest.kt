package com.stevenpopovich.talktothat.usbinterfacing

import android.hardware.usb.UsbDevice
import android.hardware.usb.UsbDeviceConnection
import android.hardware.usb.UsbManager
import com.felhr.usbserial.UsbSerialDevice
import com.stevenpopovich.talktothat.testutils.relaxedMock
import com.stevenpopovich.talktothat.testutils.verifyExactlyOne
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.verifySequence
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
        val serialPortInterface: SerialPortInterface = relaxedMock()
        val connection: UsbDeviceConnection = relaxedMock()
        val serialPort: UsbSerialDevice = relaxedMock()

        every { serialPortInterface.createConnection(usbManager, device) } returns connection
        every { serialPortInterface.createUsbSerialDevice(device, connection) } returns serialPort

        arduinoInterface.writeStringToSerialPort(usbManager, arbitraryString, device, serialPortInterface)

        verifyExactlyOne { serialPortInterface.createConnection(usbManager, device) }
        verifyExactlyOne { serialPortInterface.createUsbSerialDevice(device, connection) }
        verifyExactlyOne { serialPortInterface.writeToSerialPort(serialPort, arbitraryString) }

        confirmVerified(usbManager, device, serialPortInterface, connection, serialPort)
    }

    @Test
    fun testGetDevice() {
        val usbManager: UsbManager = relaxedMock()
        val usbDevice: UsbDevice = relaxedMock()
        val entry: MutableMap.MutableEntry<String, UsbDevice> = mutableMapOf(Pair("arb", usbDevice)).entries.first()
        val map: HashMap<String, UsbDevice> = hashMapOf(entry.toPair())

        every { usbManager.deviceList } returns map

        every { usbDevice.vendorId } returns 9025
        every { usbDevice.productId } returns 67

        val actualDevice = arduinoInterface.getDevice(usbManager)

        assertEquals(usbDevice, actualDevice)

        verifySequence {
            usbManager.deviceList.entries.firstOrNull {
                it.value.productId == arduinoInterface.PRODUCT_ID && it.value.vendorId == arduinoInterface.VENDOR_ID
            }?.value
        }

        verifySequence {
            map.entries
            entry.value
            usbDevice.productId
            entry.value
            usbDevice.vendorId
            entry.value
            usbDevice.equals(actualDevice)
        }

        confirmVerified(usbManager, usbDevice)
    }
}
