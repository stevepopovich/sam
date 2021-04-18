package com.stevenpopovich.talktothat

import android.Manifest
import android.hardware.usb.UsbManager
import android.os.Bundle
import android.speech.SpeechRecognizer
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment

private const val ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION"

class MainFragment : Fragment(R.layout.fragment_main) {
    private val arbitraryActionCode = 200

    private val usbManager: UsbManager by lazy {
        this.requireContext().getSystemService(AppCompatActivity.USB_SERVICE) as UsbManager
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

        MainDependencyModule.mainText = this.requireActivity().findViewById(R.id.main_text)
        MainDependencyModule.debugText = this.requireActivity().findViewById(R.id.debug_text)
        MainDependencyModule.camera = this.requireActivity().findViewById(R.id.camera)

        MainDependencyModule.camera.setLifecycleOwner(this.viewLifecycleOwner)

        MainDependencyModule.speechRecognizer = speechRecognizer
        MainDependencyModule.usbManager = usbManager
        MainDependencyModule.runOnUIThread = this.requireActivity()::runOnUiThread

        MainDependencyModule.inject()

        AppEngine().start()
    }
}
