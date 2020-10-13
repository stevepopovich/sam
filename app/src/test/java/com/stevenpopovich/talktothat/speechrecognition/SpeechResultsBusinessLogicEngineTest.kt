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
    fun `test passing turn on the lights in the bundle sends on to the arduino`() {
        val bundle: Bundle = relaxedMock()
        val stringArrayList: ArrayList<String> = arrayListOf("turn on the light")

        every { bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION) } returns stringArrayList

        speechResultsBusinessLogicEngine.onSpeechResults(bundle)

        verify { mainText.text = stringArrayList.toString() }
        verify { arduinoInterface.writeStringToSerialPort(serialPortInterface, "on") }

        confirmVerified(mainText, arduinoDevice, usbManager, arduinoInterface, serialPortInterface)
    }
}
