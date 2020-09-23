package com.stevenpopovich.talktothat

import android.hardware.usb.UsbManager
import android.os.Bundle
import android.speech.SpeechRecognizer
import android.widget.TextView

class SpeechResultsBusinessLogicEngine(
    private val mainText: TextView,
    private val usbManager: UsbManager,
    private val arduinoInterface: ArduinoInterface,
    private val serialPortWriter: SerialPortWriter
) {
    fun onSpeechResults(results: Bundle?) {
        val speechResults = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)

        mainText.text = speechResults.toString()

        arduinoInterface.getDevice(usbManager)?.let { arduino ->
            if (speechResults?.any { it.contains("turn on the light") } == true) {
                arduinoInterface.writeStringToSerialPort(usbManager, "on", arduino, serialPortWriter)
            }
        }
    }
}
