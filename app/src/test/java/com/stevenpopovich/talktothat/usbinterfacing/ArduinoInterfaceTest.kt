package com.stevenpopovich.talktothat.usbinterfacing

import android.hardware.usb.UsbDevice
import android.hardware.usb.UsbManager
import com.stevenpopovich.talktothat.testutils.relaxedMock
import io.mockk.every
import io.mockk.verify
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
    fun `test we can get a device`() {
        val usbManager: UsbManager = relaxedMock()
        val usbDevice: UsbDevice = relaxedMock()
        val entry: MutableMap.MutableEntry<String, UsbDevice> = mutableMapOf(Pair("arb", usbDevice)).entries.first()
        val map: HashMap<String, UsbDevice> = hashMapOf(entry.toPair())

        every { usbDevice.vendorId } returns 9025
        every { usbDevice.productId } returns 67

        every { usbManager.deviceList } returns map

        arduinoInterface.getDevice(usbManager)
    }

    @Test
    fun `test write to serial port`() {
        val serialPortInterface: SerialPortInterface = relaxedMock()
        val stringToWrite = "thingy"

        arduinoInterface.writeStringToSerialPort(serialPortInterface, stringToWrite)

        verify { serialPortInterface.writeToSerialPort(stringToWrite + "\n") }
    }
}
