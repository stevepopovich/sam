package com.stevenpopovich.talktothat

import android.Manifest
import android.hardware.usb.UsbManager
import android.os.Bundle
import android.speech.SpeechRecognizer
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat

class MainActivity : AppCompatActivity() {
    private val ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION"

    private val usbManager: UsbManager by lazy {
        applicationContext.getSystemService(USB_SERVICE) as UsbManager
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ActivityCompat.requestPermissions(
            this,
            listOf(Manifest.permission.RECORD_AUDIO, ACTION_USB_PERMISSION).toTypedArray(),
            200
        )
    }

    override fun onResume() {
        super.onResume()

        ContinuousSpeechRecognizer.instance.startListening(::onSpeechResults, applicationContext)
    }

    private fun onSpeechResults(results: Bundle?) {
        val speechResults = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)

        speechResults.toString().verboseLog()

        findViewById<TextView>(R.id.main_text).text = speechResults.toString()

        if (speechResults?.any { it.contains("turn on the light") } == true) {
            ArduinoInterface.instance.writeStringToSerialPort(usbManager, "on")
        }
    }
}
