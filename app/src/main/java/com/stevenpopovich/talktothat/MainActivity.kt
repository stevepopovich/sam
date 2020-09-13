package com.stevenpopovich.talktothat

import android.Manifest
import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.hardware.usb.UsbDevice
import android.hardware.usb.UsbManager
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import android.view.MotionEvent
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private val ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION"

    private val usbManager: UsbManager by lazy {
        applicationContext.getSystemService(USB_SERVICE) as UsbManager
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ActivityCompat.requestPermissions(this,
            listOf(Manifest.permission.RECORD_AUDIO, ACTION_USB_PERMISSION).toTypedArray(),
            200)
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

