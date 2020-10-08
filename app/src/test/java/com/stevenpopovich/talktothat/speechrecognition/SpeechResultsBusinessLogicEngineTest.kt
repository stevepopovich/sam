package com.stevenpopovich.talktothat.speechrecognition

import android.hardware.usb.UsbDevice
import android.hardware.usb.UsbManager
import android.os.Bundle
import android.speech.SpeechRecognizer
import android.widget.TextView
import com.stevenpopovich.talktothat.testutils.relaxedMock
import com.stevenpopovich.talktothat.usbinterfacing.ArduinoInterface
import com.stevenpopovich.talktothat.usbinterfacing.SerialPortWriter
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.verify
import org.junit.Test

class SpeechResultsBusinessLogicEngineTest {
    private val mainText: TextView = relaxedMock()
    private val arduinoDevice: UsbDevice = relaxedMock()
    private val usbManager: UsbManager = relaxedMock()
    private val arduinoInterface: ArduinoInterface = relaxedMock()
    private val serialPortWriter: SerialPortWriter = relaxedMock()

    private val speechResultsBusinessLogicEngine = SpeechResultsBusinessLogicEngine(
        mainText,
        usbManager,
        arduinoInterface,
        serialPortWriter
    )

    @Test
    fun `test passing turn on the lights in the bundle sends on to the arduino`() {
        val bundle: Bundle = relaxedMock()
        val stringArrayList: ArrayList<String> = arrayListOf("turn on the light")
        every { bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION) } returns stringArrayList
        every { arduinoInterface.getDevice(usbManager) } returns arduinoDevice

        speechResultsBusinessLogicEngine.onSpeechResults(bundle)

        verify { mainText.text = stringArrayList.toString() }
        verify { arduinoInterface.getDevice(usbManager) }
        verify { arduinoInterface.writeStringToSerialPort(usbManager, "on", arduinoDevice!!, serialPortWriter) }

        confirmVerified(mainText, arduinoDevice, usbManager, arduinoInterface, serialPortWriter)
    }

    @Test
    fun `test optional parameters`() {
        SpeechResultsBusinessLogicEngine(mainText, usbManager)
    }
}
