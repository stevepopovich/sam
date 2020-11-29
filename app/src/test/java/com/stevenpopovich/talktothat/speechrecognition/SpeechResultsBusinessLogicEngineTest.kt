package com.stevenpopovich.talktothat.speechrecognition

import android.hardware.usb.UsbDevice
import android.hardware.usb.UsbManager
import android.os.Bundle
import android.speech.SpeechRecognizer
import android.widget.TextView
import com.stevenpopovich.talktothat.MainFragment
import com.stevenpopovich.talktothat.testutils.relaxedMock
import com.stevenpopovich.talktothat.usbinterfacing.ArduinoInterface
import com.stevenpopovich.talktothat.usbinterfacing.SerialPortInterface
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.verify
import org.junit.Test

class SpeechResultsBusinessLogicEngineTest {
    private val mainText: TextView = relaxedMock()
    private val arduinoDevice: UsbDevice = relaxedMock()
    private val usbManager: UsbManager = relaxedMock()
    private val arduinoInterface: ArduinoInterface = relaxedMock()
    private val serialPortInterface: SerialPortInterface = relaxedMock()
    private val debugTextView: TextView = relaxedMock()
    private val mainFragment: MainFragment = relaxedMock()

    private val speechResultsBusinessLogicEngine = SpeechResultsBusinessLogicEngine(
        mainText,
        usbManager,
        debugTextView,
        mainFragment,
        arduinoInterface,
        serialPortInterface
    )

    @Test
    fun `test passing forward, backward, left, right, stop passes those strings to the arduino`() {
        val bundle: Bundle = relaxedMock()
        val stringArrayList: ArrayList<String> = arrayListOf("forward, backward, left, right, stop")

        every { bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION) } returns stringArrayList

        speechResultsBusinessLogicEngine.onSpeechResults(bundle)

        verify { mainText.text = stringArrayList.toString() }
        verify { arduinoInterface.writeStringToSerialPort(serialPortInterface, "forward") }
        verify { arduinoInterface.writeStringToSerialPort(serialPortInterface, "backward") }
        verify { arduinoInterface.writeStringToSerialPort(serialPortInterface, "left") }
        verify { arduinoInterface.writeStringToSerialPort(serialPortInterface, "right") }
        verify { arduinoInterface.writeStringToSerialPort(serialPortInterface, "stop") }

        confirmVerified(mainText, arduinoDevice, usbManager, arduinoInterface, serialPortInterface)
    }

    @Test
    fun `test optional parameters`() {
        val bundle: Bundle = relaxedMock()

        val engine = SpeechResultsBusinessLogicEngine(
            mainText,
            usbManager,
            debugTextView,
            mainFragment
        )

        engine.onSpeechResults(bundle)
    }
}
