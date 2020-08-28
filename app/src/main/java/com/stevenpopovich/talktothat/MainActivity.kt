package com.stevenpopovich.talktothat

import android.Manifest
import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.res.Resources
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
import com.felhr.usbserial.UsbSerialDevice
import com.felhr.usbserial.UsbSerialInterface
import java.util.*
import kotlin.collections.HashMap

class MainActivity : AppCompatActivity() {
    private val ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ActivityCompat.requestPermissions(this,
            listOf(Manifest.permission.RECORD_AUDIO).toTypedArray(),
            200)
    }

    override fun onResume() {
        super.onResume()

        val usbManager = applicationContext.getSystemService(USB_SERVICE) as UsbManager
        val usbDevices = usbManager.deviceList

        this.findViewById<TextView>(R.id.number_of_devices).text = "Device count: ${usbDevices?.size} and devices is null: ${usbDevices == null}"
        this.findViewById<TextView>(R.id.vendor_id).text = "Keys: ${usbDevices?.keys}"

        if (usbDevices?.keys?.isNotEmpty() == true) {
            val firstKey = usbDevices.keys.first()
            if (firstKey != null) {
                val usbDevice = usbDevices.get(firstKey)

                setupListening(usbDevice!!)

                this.findViewById<TextView>(R.id.vendor_id).text = "Vendor Id: ${usbDevice?.vendorId}"
                this.findViewById<TextView>(R.id.product_id).text = "Product Id: ${usbDevice?.productId}"

                val permissionIntent = PendingIntent.getBroadcast(this, 0, Intent(ACTION_USB_PERMISSION), 0)
                val filter = IntentFilter(ACTION_USB_PERMISSION)
                registerReceiver(usbReceiver, filter)

                usbManager.requestPermission(usbDevice, permissionIntent)
            }
        }

        this.findViewById<Button>(R.id.light_button).setOnClickListener {
            if (usbDevices?.keys?.isNotEmpty() == true) {
                val firstKey = usbDevices.keys.first()
                if (firstKey != null) {
                    val usbDevice = usbDevices[firstKey]
                    lightUpArdy(usbDevice!!)
                }
            }
        }
    }

    private val usbReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (ACTION_USB_PERMISSION == intent.action) {
                synchronized(this) {
                    val device: UsbDevice? = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE)

                    if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                        lightUpArdy(device!!)
                    } else {
                        Log.d("STEV", "permission denied for device $device")
                    }
                }
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupListening(device: UsbDevice) {
        val speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this)
        speechRecognizer.setRecognitionListener(object : RecognitionListener {
            override fun onReadyForSpeech(params: Bundle?) {}
            override fun onBeginningOfSpeech() {}
            override fun onRmsChanged(rmsdB: Float) {}
            override fun onBufferReceived(buffer: ByteArray?) {}
            override fun onEndOfSpeech() {}
            override fun onError(error: Int) {}

            override fun onResults(results: Bundle?) {
                val speechResults = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)

                findViewById<TextView>(R.id.product_id).text = speechResults.toString()

                if (speechResults?.any { it.contains("turn on the light") } == true) {
                    lightUpArdy(device)
                }
            }

            override fun onPartialResults(partialResults: Bundle?) {}
            override fun onEvent(eventType: Int, params: Bundle?) {}
        })

        val speechRecognizerIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

        findViewById<Button>(R.id.listen_button).setOnTouchListener { view, event ->
            when (event.action) {
                MotionEvent.ACTION_UP -> {
                    view.performClick()
                    speechRecognizer.stopListening()
                    findViewById<ConstraintLayout>(R.id.container).setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.white))
                }
                MotionEvent.ACTION_DOWN -> {
                    speechRecognizer.startListening(speechRecognizerIntent)
                    findViewById<ConstraintLayout>(R.id.container).setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.dark_gray))
                }
            }
           true
        }
    }

    private fun lightUpArdy(device: UsbDevice) {
        val usbManager = applicationContext.getSystemService(USB_SERVICE) as UsbManager

        val connection = usbManager.openDevice(device)
        val serialPort = UsbSerialDevice.createUsbSerialDevice(device, connection)
        serialPort.open()
        serialPort.setBaudRate(9600)
        serialPort.setDataBits(UsbSerialInterface.DATA_BITS_8)
        serialPort.setStopBits(UsbSerialInterface.STOP_BITS_1)
        serialPort.setParity(UsbSerialInterface.PARITY_NONE)
        serialPort.setFlowControl(UsbSerialInterface.FLOW_CONTROL_OFF)
        serialPort.write("on".toByteArray())
        serialPort.close()
    }
}

