package com.stevenpopovich.talktothat.taskmanager.tasks

import com.stevenpopovich.talktothat.testutils.relaxedMock
import com.stevenpopovich.talktothat.usbinterfacing.ArduinoInterface
import com.stevenpopovich.talktothat.usbinterfacing.SerialPortInterface
import io.mockk.confirmVerified
import io.mockk.verify
import io.mockk.verifySequence
import org.junit.Test

class DoASpinTaskTest {
    private val arduinoInterface: ArduinoInterface = relaxedMock()
    private val serialPortInterface: SerialPortInterface = relaxedMock()

    private val doASpinTask = DoASpinTask(arduinoInterface, serialPortInterface)
    @Test
    fun `start sends donttouch to the arduino`() {
        doASpinTask.start()

        Thread.sleep(3000)

        verifySequence {
            arduinoInterface.writeStringToSerialPort(serialPortInterface, "-50")
            arduinoInterface.writeStringToSerialPort(serialPortInterface, "225")
            arduinoInterface.writeStringToSerialPort(serialPortInterface, "stop")
        }

        confirmVerified(arduinoInterface, serialPortInterface)
    }

    @Test
    fun `finish sends stop to the arduino`() {
        doASpinTask.finish()

        verify { arduinoInterface.writeStringToSerialPort(serialPortInterface, "stop") }

        confirmVerified(arduinoInterface, serialPortInterface)
    }
}
