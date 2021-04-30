package com.stevenpopovich.talktothat

import android.hardware.usb.UsbManager
import android.speech.SpeechRecognizer
import android.widget.TextView
import com.google.mlkit.vision.objects.ObjectDetector
import com.google.mlkit.vision.objects.defaults.ObjectDetectorOptions
import com.otaliastudios.cameraview.CameraView
import com.stevenpopovich.talktothat.cameraengine.CameraEngine
import com.stevenpopovich.talktothat.cameraengine.facialdetection.FaceDetectionSuccessListener
import com.stevenpopovich.talktothat.cameraengine.facialdetection.FaceTrackingEngine
import com.stevenpopovich.talktothat.cameraengine.objecttracker.DetectedObjectSuccessListener
import com.stevenpopovich.talktothat.cameraengine.objecttracker.ObjectTracker
import com.stevenpopovich.talktothat.cameraengine.objecttracker.getDetector
import com.stevenpopovich.talktothat.speechrecognition.ContinuousSpeechRecognizer
import com.stevenpopovich.talktothat.speechrecognition.SpeechResultBusinessLogic
import com.stevenpopovich.talktothat.speechrecognition.SpeechResultsBusinessLogicEngine
import com.stevenpopovich.talktothat.taskmanager.TaskManager
import com.stevenpopovich.talktothat.usbinterfacing.ArduinoInterface
import com.stevenpopovich.talktothat.usbinterfacing.OnReadFromSerialPortLogic
import com.stevenpopovich.talktothat.usbinterfacing.SerialPortInterface
import com.stevenpopovich.talktothat.usbinterfacing.SerialPortReader
import com.stevenpopovich.talktothat.usbinterfacing.getSerialPortInterface

class MainDependencyModule {
    companion object {
        lateinit var mainText: TextView
        lateinit var debugText: TextView
        lateinit var camera: CameraView
        lateinit var usbManager: UsbManager
        lateinit var speechRecognizer: SpeechRecognizer
        lateinit var runOnUIThread: (action: Runnable) -> Unit

        val taskManager = TaskManager()

        fun inject() {
            onObjectDetectedSuccessListener = DetectedObjectSuccessListener()
            objectDetector = getDetector(ObjectDetectorOptions.Builder())
            objectTracker = ObjectTracker()

            arduinoInterface = ArduinoInterface()
            onReadFromSerialPortLogic = OnReadFromSerialPortLogic()
            serialPortReader = SerialPortReader()
            serialPortInterface = getSerialPortInterface()
            faceDetectionSuccessListener = FaceDetectionSuccessListener()
            faceTrackingEngine = FaceTrackingEngine()
            cameraEngine = CameraEngine()

            continuousSpeechRecognizer = ContinuousSpeechRecognizer()
            speechResultsBusinessLogicEngine = SpeechResultsBusinessLogicEngine()
            speechResultsBusinessLogic = speechResultsBusinessLogicEngine::onSpeechResults
        }

        lateinit var objectTracker: ObjectTracker
        lateinit var onObjectDetectedSuccessListener: DetectedObjectSuccessListener
        lateinit var objectDetector: ObjectDetector

        lateinit var faceDetectionSuccessListener: FaceDetectionSuccessListener
        lateinit var faceTrackingEngine: FaceTrackingEngine
        lateinit var cameraEngine: CameraEngine

        lateinit var continuousSpeechRecognizer: ContinuousSpeechRecognizer
        lateinit var speechResultsBusinessLogicEngine: SpeechResultsBusinessLogicEngine
        lateinit var speechResultsBusinessLogic: SpeechResultBusinessLogic

        var serialPortInterface: SerialPortInterface? = null
        lateinit var serialPortReader: SerialPortReader
        lateinit var onReadFromSerialPortLogic: OnReadFromSerialPortLogic
        lateinit var arduinoInterface: ArduinoInterface
    }
}
