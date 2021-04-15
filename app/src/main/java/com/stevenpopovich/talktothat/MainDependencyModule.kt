package com.stevenpopovich.talktothat

import android.hardware.usb.UsbManager
import android.speech.SpeechRecognizer
import android.widget.TextView
import com.felhr.usbserial.UsbSerialDevice
import com.otaliastudios.cameraview.CameraView
import com.stevenpopovich.talktothat.cameraengine.CameraEngine
import com.stevenpopovich.talktothat.cameraengine.facialdetection.FaceDetectionEngine
import com.stevenpopovich.talktothat.cameraengine.objecttracker.ObjectTracker
import com.stevenpopovich.talktothat.speechrecognition.ContinuousSpeechRecognizer
import com.stevenpopovich.talktothat.speechrecognition.SpeechRecognizerRestartLogic
import com.stevenpopovich.talktothat.speechrecognition.SpeechResultBusinessLogic
import com.stevenpopovich.talktothat.speechrecognition.SpeechResultsBusinessLogicEngine
import com.stevenpopovich.talktothat.taskmanager.TaskManager
import com.stevenpopovich.talktothat.usbinterfacing.ArduinoInterface
import com.stevenpopovich.talktothat.usbinterfacing.OnReadFromSerialPortLogic
import com.stevenpopovich.talktothat.usbinterfacing.SerialPortInterface
import com.stevenpopovich.talktothat.usbinterfacing.SerialPortReader

class MainDependencyModule {
    companion object {
        lateinit var mainText: TextView
        lateinit var debugText: TextView
        lateinit var camera: CameraView
        lateinit var usbManager: UsbManager
        lateinit var speechRecognizer: SpeechRecognizer
        lateinit var runOnUIThread: (action: Runnable) -> Unit
        lateinit var serialPortInterface: SerialPortInterface

        val taskManager = TaskManager()

        fun inject() {
        }

        lateinit var continuousSpeechRecognizer: ContinuousSpeechRecognizer
        lateinit var speechResultsBusinessLogicEngine: SpeechResultsBusinessLogicEngine
        lateinit var serialPortReader: SerialPortReader
        lateinit var serialDevice: UsbSerialDevice
        lateinit var cameraEngine: CameraEngine
        lateinit var onReadFromSerialPortLogic: OnReadFromSerialPortLogic
        lateinit var arduinoInterface: ArduinoInterface
        lateinit var speechResultsBusinessLogic: SpeechResultBusinessLogic
        lateinit var speechRecognizerRestartLogic: SpeechRecognizerRestartLogic
        lateinit var objectTracker: ObjectTracker
        lateinit var faceDetectionEngine: FaceDetectionEngine
    }
}
