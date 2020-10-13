package com.stevenpopovich.talktothat.speechrecognition

import android.hardware.usb.UsbManager
import android.os.Bundle
import android.speech.SpeechRecognizer
import android.widget.TextView
import com.stevenpopovich.talktothat.MainFragment
import com.stevenpopovich.talktothat.usbinterfacing.ArduinoInterface
import com.stevenpopovich.talktothat.usbinterfacing.SerialPortInterface
import com.stevenpopovich.talktothat.usbinterfacing.SerialPortInterfaceBuilder
import com.stevenpopovich.talktothat.usbinterfacing.SerialPortReader

class SpeechResultsBusinessLogicEngine(
    private val mainText: TextView,
    usbManager: UsbManager,
    debugTextView: TextView,
    mainFragment: MainFragment,
    private val arduinoInterface: ArduinoInterface = ArduinoInterface(),
    private val serialPortInterface: SerialPortInterface? =
        SerialPortInterfaceBuilder().getSerialPortInterface(
            SerialPortReader(debugTextView, mainFragment),
            usbManager,
            arduinoInterface
        )
) {
    fun onSpeechResults(results: Bundle?) {
        serialPortInterface?.let {
            val speechResults = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)

            mainText.text = speechResults.toString()

            if (speechResults?.any { it.contains("turn on the light") } == true) {
                arduinoInterface.writeStringToSerialPort(it, "on")
            }
        }
    }
}
