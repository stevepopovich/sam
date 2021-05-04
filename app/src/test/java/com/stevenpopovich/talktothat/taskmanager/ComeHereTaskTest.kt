package com.stevenpopovich.talktothat.taskmanager

import com.stevenpopovich.talktothat.testutils.relaxedMock
import com.stevenpopovich.talktothat.usbinterfacing.ArduinoInterface
import com.stevenpopovich.talktothat.usbinterfacing.SerialPortInterface
import io.mockk.verify
import org.junit.Test

class ComeHereTaskTest {

    private val arduinoInterface: ArduinoInterface = relaxedMock()
    private val serialPortInterface: SerialPortInterface = relaxedMock()

    private val comeHereTask = ComeHereTask(arduinoInterface, serialPortInterface)
    @Test
    fun `start sends forward to the arduino`() {
        comeHereTask.start()

        verify { arduinoInterface.writeStringToSerialPort(serialPortInterface, "forward") }
    }

    @Test
    fun `finish sends stop to the arduino`() {
        comeHereTask.finish()

        verify { arduinoInterface.writeStringToSerialPort(serialPortInterface, "stop") }
    }
}
