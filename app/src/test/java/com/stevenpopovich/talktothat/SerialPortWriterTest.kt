package com.stevenpopovich.talktothat

import android.hardware.usb.UsbDevice
import android.hardware.usb.UsbDeviceConnection
import android.hardware.usb.UsbManager
import com.felhr.usbserial.UsbSerialDevice
import com.felhr.usbserial.UsbSerialInterface
import io.mockk.every
import io.mockk.mockkStatic
import io.mockk.verify
import io.mockk.verifyAll
import org.junit.Assert
import org.junit.Test

class SerialPortWriterTest {
    private val serialPortWriter = SerialPortWriter()

    @Test
    fun testCreateConnection() {
        val usbManager: UsbManager = relaxedMock()
        val device: UsbDevice = relaxedMock()

        val expectedConnection = usbManager.openDevice(device)

        val actualConnection = serialPortWriter.createConnection(usbManager, device)

        verify(exactly = 2) { usbManager.openDevice(device) }
        Assert.assertEquals(expectedConnection, actualConnection)
    }

    @Test
    fun testCreateSerialPort() {
        val connection: UsbDeviceConnection = relaxedMock()
        val device: UsbDevice = relaxedMock()
        val expectedSerialPort: UsbSerialDevice = relaxedMock()

        mockkStatic("com.felhr.usbserial.UsbSerialDevice")

        every { UsbSerialDevice.createUsbSerialDevice(device, connection) } returns expectedSerialPort

        val actualSerialPort = serialPortWriter.createSerialPort(device, connection)

        verifyExactlyOne { UsbSerialDevice.createUsbSerialDevice(device, connection) }
        Assert.assertEquals(expectedSerialPort, actualSerialPort)
    }

    @Test
    fun testWriteToSerialPort() {
        val serialPort: UsbSerialDevice = relaxedMock()
        val arbitraryString = "Ring a ding dong"

        serialPortWriter.writeToSerialPort(serialPort, arbitraryString)

        verifyAll {
            serialPort.open()
            serialPort.setBaudRate(9600)
            serialPort.setDataBits(UsbSerialInterface.DATA_BITS_8)
            serialPort.setStopBits(UsbSerialInterface.STOP_BITS_1)
            serialPort.setParity(UsbSerialInterface.PARITY_NONE)
            serialPort.setFlowControl(UsbSerialInterface.FLOW_CONTROL_OFF)
            serialPort.write(arbitraryString.toByteArray())
            serialPort.close()
        }
    }
}
