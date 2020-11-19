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
        serialPortInterface?.let { serialPort ->
            val speechResults = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)

            mainText.text = speechResults.toString()

            if (speechResults?.any { it.contains("forward") } == true) {
                arduinoInterface.writeStringToSerialPort(serialPort, "forward")
            }
            if (speechResults?.any { it.contains("backward") } == true) {
                arduinoInterface.writeStringToSerialPort(serialPort, "backward")
            }
            if (speechResults?.any { it.contains("stop") } == true) {
                arduinoInterface.writeStringToSerialPort(serialPort, "stop")
            }
            if (speechResults?.any { it.contains("left") } == true) {
                arduinoInterface.writeStringToSerialPort(serialPort, "left")
            }
            if (speechResults?.any { it.contains("right") } == true) {
                arduinoInterface.writeStringToSerialPort(serialPort, "right")
            }
        }
    }
}
