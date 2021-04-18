package com.stevenpopovich.talktothat.usbinterfacing

import android.hardware.usb.UsbDevice
import android.hardware.usb.UsbDeviceConnection
import android.hardware.usb.UsbManager
import com.felhr.usbserial.UsbSerialDevice
import com.stevenpopovich.talktothat.MainDependencyModule
import com.stevenpopovich.talktothat.testutils.relaxedMock
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockkStatic
import io.mockk.verify
import io.mockk.verifySequence
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.lang.NullPointerException

class SerialPortInterfaceBuilderTest {

    @Before
    fun setup() {
        MainDependencyModule.serialPortReader = relaxedMock()
    }

    @Test
    fun `test that we get device and open it`() {
        mockkStatic("com.felhr.usbserial.UsbSerialDevice")

        val usbManager: UsbManager = relaxedMock()
        val usbDevice: UsbDevice = relaxedMock()

        val arduinoInterface: ArduinoInterface = relaxedMock()
        every { arduinoInterface.getDevice(usbManager) } returns usbDevice

        val usbDeviceConnection: UsbDeviceConnection = relaxedMock()
        every { usbManager.openDevice(usbDevice) } returns usbDeviceConnection

        val serialDevice: UsbSerialDevice = relaxedMock()
        every { UsbSerialDevice.createUsbSerialDevice(usbDevice, usbDeviceConnection) } returns serialDevice

        val serialPort = getSerialPortInterface(usbManager, arduinoInterface)

        verifySequence {
            arduinoInterface.getDevice(usbManager)
            usbManager.openDevice(usbDevice)
            UsbSerialDevice.createUsbSerialDevice(usbDevice, usbDeviceConnection)
        }

        confirmVerified(usbManager, MainDependencyModule.serialPortReader, usbDevice, usbDeviceConnection, arduinoInterface)

        assertTrue(serialPort != null)
    }

    @Test
    fun `test that if we have no devices, we return explode`() {
        val usbManager: UsbManager = relaxedMock()
        val arduinoInterface: ArduinoInterface = relaxedMock()
        every { arduinoInterface.getDevice(usbManager) } returns null

        try {
            getSerialPortInterface(usbManager, arduinoInterface)
        } catch (e: NullPointerException) {
            verify { arduinoInterface.getDevice(usbManager) }

            confirmVerified(usbManager, MainDependencyModule.serialPortReader, arduinoInterface)
        }
    }
}
