package com.stevenpopovich.talktothat.taskmanager.tasks

import com.stevenpopovich.talktothat.testutils.relaxedMock
import com.stevenpopovich.talktothat.usbinterfacing.ArduinoInterface
import com.stevenpopovich.talktothat.usbinterfacing.SerialPortInterface
import io.mockk.confirmVerified
import io.mockk.verify
import org.junit.Test

class DontTouchTaskTest {
    private val arduinoInterface: ArduinoInterface = relaxedMock()
    private val serialPortInterface: SerialPortInterface = relaxedMock()

    private val dontTouchTask = DontTouchTask(arduinoInterface, serialPortInterface)
    @Test
    fun `start sends donttouch to the arduino`() {
        dontTouchTask.start()

        verify { arduinoInterface.writeStringToSerialPort(serialPortInterface, "donttouch") }

        confirmVerified(arduinoInterface, serialPortInterface)
    }

    @Test
    fun `finish sends stop to the arduino`() {
        dontTouchTask.finish()

        verify { arduinoInterface.writeStringToSerialPort(serialPortInterface, "stop") }

        confirmVerified(arduinoInterface, serialPortInterface)
    }
}
