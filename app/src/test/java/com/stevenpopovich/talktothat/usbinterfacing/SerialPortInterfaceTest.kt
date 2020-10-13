package com.stevenpopovich.talktothat.usbinterfacing

import com.felhr.usbserial.UsbSerialDevice
import com.felhr.usbserial.UsbSerialInterface
import com.stevenpopovich.talktothat.testutils.relaxedMock
import io.mockk.confirmVerified
import io.mockk.verify
import io.mockk.verifySequence
import org.junit.Test

class SerialPortInterfaceTest {

    @Test
    fun `test that creating interface opens port`() {
        val usbSerialDevice: UsbSerialDevice = relaxedMock()
        val serialPortReader: SerialPortReader = relaxedMock()

        SerialPortInterface(serialPortReader, usbSerialDevice)

        verifySequence {
            usbSerialDevice.open()
            usbSerialDevice.setBaudRate(9600)
            usbSerialDevice.setDataBits(UsbSerialInterface.DATA_BITS_8)
            usbSerialDevice.setStopBits(UsbSerialInterface.STOP_BITS_1)
            usbSerialDevice.setParity(UsbSerialInterface.PARITY_NONE)
            usbSerialDevice.setFlowControl(UsbSerialInterface.FLOW_CONTROL_OFF)
            usbSerialDevice.read(serialPortReader)
        }

        confirmVerified(usbSerialDevice, serialPortReader)
    }

    @Test
    fun `test writing to serial device`() {
        val usbSerialDevice: UsbSerialDevice = relaxedMock()
        val serialPortReader: SerialPortReader = relaxedMock()
        val stringToWrite = "thing"

        val serialPortInterface = SerialPortInterface(serialPortReader, usbSerialDevice)

        serialPortInterface.writeToSerialPort(stringToWrite)

        verify { usbSerialDevice.write(stringToWrite.toByteArray()) }
    }
}
