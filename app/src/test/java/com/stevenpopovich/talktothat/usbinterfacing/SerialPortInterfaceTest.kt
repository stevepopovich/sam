package com.stevenpopovich.talktothat.usbinterfacing

import android.hardware.usb.UsbDevice
import android.hardware.usb.UsbDeviceConnection
import android.hardware.usb.UsbManager
import com.felhr.usbserial.UsbSerialDevice
import com.felhr.usbserial.UsbSerialInterface
import com.stevenpopovich.talktothat.testutils.relaxedMock
import com.stevenpopovich.talktothat.testutils.verifyExactlyOne
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockkStatic
import io.mockk.verify
import io.mockk.verifySequence
import org.junit.Assert
import org.junit.Test

class SerialPortInterfaceTest {
    private val serialPortWriter = SerialPortInterface()

    @Test
    fun testCreateConnection() {
        val usbManager: UsbManager = relaxedMock()
        val device: UsbDevice = relaxedMock()

        val expectedConnection = usbManager.openDevice(device)

        val actualConnection = serialPortWriter.createConnection(usbManager, device)

        verify(exactly = 2) { usbManager.openDevice(device) }
        Assert.assertEquals(expectedConnection, actualConnection)

        confirmVerified(usbManager, device)
    }

    @Test
    fun testCreateSerialPort() {
        val connection: UsbDeviceConnection = relaxedMock()
        val device: UsbDevice = relaxedMock()
        val expectedSerialPort: UsbSerialDevice = relaxedMock()

        mockkStatic("com.felhr.usbserial.UsbSerialDevice")

        every { UsbSerialDevice.createUsbSerialDevice(device, connection) } returns expectedSerialPort

        val actualSerialPort = serialPortWriter.createUsbSerialDevice(device, connection)

        verifyExactlyOne { UsbSerialDevice.createUsbSerialDevice(device, connection) }
        Assert.assertEquals(expectedSerialPort, actualSerialPort)

        verify { expectedSerialPort.equals(actualSerialPort) }

        confirmVerified(connection, device, expectedSerialPort)
    }

    @Test
    fun testWriteToSerialPort() {
        val serialPort: UsbSerialDevice = relaxedMock()
        val arbitraryString = "Ring a ding dong"

        serialPortWriter.writeToSerialPort(serialPort, arbitraryString)

        verifySequence {
            serialPort.open()
            serialPort.setBaudRate(9600)
            serialPort.setDataBits(UsbSerialInterface.DATA_BITS_8)
            serialPort.setStopBits(UsbSerialInterface.STOP_BITS_1)
            serialPort.setParity(UsbSerialInterface.PARITY_NONE)
            serialPort.setFlowControl(UsbSerialInterface.FLOW_CONTROL_OFF)
            serialPort.write(arbitraryString.toByteArray())
            serialPort.close()
        }

        confirmVerified(serialPort)
    }
}
