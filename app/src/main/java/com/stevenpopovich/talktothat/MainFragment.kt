package com.stevenpopovich.talktothat

import android.Manifest
import android.hardware.camera2.CameraManager
import android.hardware.usb.UsbManager
import android.os.Bundle
import android.speech.SpeechRecognizer
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.otaliastudios.cameraview.CameraView

class MainFragment : Fragment(R.layout.fragment_main) {
    private val ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION"
    private val arbitraryActionCode = 200

    private val usbManager: UsbManager by lazy {
        this.requireContext().getSystemService(AppCompatActivity.USB_SERVICE) as UsbManager
    }

    private val cameraManager: CameraManager by lazy {
        this.requireContext().getSystemService(AppCompatActivity.CAMERA_SERVICE) as CameraManager
    }

    private val speechRecognizer: SpeechRecognizer by lazy {
        SpeechRecognizer.createSpeechRecognizer(this.requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ActivityCompat.requestPermissions(
            this.requireActivity(),
            listOf(
                Manifest.permission.RECORD_AUDIO,
                ACTION_USB_PERMISSION,
                Manifest.permission.CAMERA
            ).toTypedArray(),
            arbitraryActionCode
        )
    }

    override fun onResume() {
        super.onResume()

        val mainText: TextView = this.requireActivity().findViewById(R.id.main_text)
        val camera: CameraView = this.requireActivity().findViewById(R.id.camera)

        camera.setLifecycleOwner(this.viewLifecycleOwner)

        val speechResultsBusinessLogicEngine = SpeechResultsBusinessLogicEngine(
            mainText,
            usbManager,
            ArduinoInterface(),
            SerialPortWriter()
        )

        AppEngine().start(
            ContinuousSpeechRecognizer(speechRecognizer),
            speechResultsBusinessLogicEngine,
            CameraEngine(camera, ObjectTracker(camera))
        )
    }
}
