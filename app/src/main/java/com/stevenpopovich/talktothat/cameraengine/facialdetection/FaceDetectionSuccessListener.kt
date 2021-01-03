package com.stevenpopovich.talktothat.cameraengine.facialdetection

import android.graphics.Rect
import android.hardware.usb.UsbManager
import android.widget.TextView
import com.google.android.gms.tasks.OnSuccessListener
import com.google.mlkit.vision.face.Face
import com.otaliastudios.cameraview.CameraView
import com.stevenpopovich.talktothat.MainFragment
import com.stevenpopovich.talktothat.cameraengine.RectangleDrawable
import com.stevenpopovich.talktothat.cameraengine.moveXOneThirdRight
import com.stevenpopovich.talktothat.usbinterfacing.ArduinoInterface
import com.stevenpopovich.talktothat.usbinterfacing.SerialPortInterface
import com.stevenpopovich.talktothat.usbinterfacing.SerialPortInterfaceBuilder
import com.stevenpopovich.talktothat.usbinterfacing.SerialPortReader
import kotlin.math.absoluteValue

class FaceDetectionSuccessListener(
    private val cameraView: CameraView,
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
) : OnSuccessListener<MutableList<Face>> {
    override fun onSuccess(faces: MutableList<Face>?) {
        if (faces?.isNullOrEmpty() != false) {
            cameraView.overlay.clear()
            serialPortInterface?.let { serialPortInterface ->
                arduinoInterface.writeStringToSerialPort(serialPortInterface, 0.toString())
            }
        } else {
            faces?.firstOrNull()?.let { face ->
                val movedBoundingBox = face.boundingBox.moveXOneThirdRight() // WTF
                drawFaces(mutableListOf(movedBoundingBox))
                getSpinSpeedByXDistance(movedBoundingBox)
                serialPortInterface?.let { serialPortInterface ->
                    arduinoInterface.writeStringToSerialPort(
                        serialPortInterface,
                        getSpinSpeedByXDistance(movedBoundingBox).toString()
                    )
                }
            }
        }
    }

    // Example calculation:
    // The screen is 1000 across so we want the center of the face to be at 500.
    // Say the face is at 800 X. The center of the screen is 500 so the 800-500=300.
    // We need this as a proportion of how far the face is from the center of the screen.
    // So 300/500 = .6.
    // We then map that .6 to a value between -255 and 255, because thats what the arduino motors take
    // So .6 * 255 = 153, which is what we send to the arduino.
    // Obviously there needs to be a bit of tuning, but this is the idea.
    // This is the P in PID controller, which you can google.
    private fun getSpinSpeedByXDistance(face: Rect): Int {
        val centerOfCameraX = cameraView.width / 2
        val distance = face.centerX() - centerOfCameraX
        val distanceAsProportionFromCenter = distance.toDouble() / centerOfCameraX.toDouble()

        if (distanceAsProportionFromCenter.absoluteValue < .25) return 0

        return if (distanceAsProportionFromCenter > 0) {
            70
        } else {
            -70
        }
//        val proportionMappedToArduinoSpeeds = distanceAsProportionFromCenter * 140
//        val scaledArduinoSpeed = if (proportionMappedToArduinoSpeeds > 0)
//            proportionMappedToArduinoSpeeds + 50
//        else
//            proportionMappedToArduinoSpeeds - 50

//        return -scaledArduinoSpeed.toInt()
    }

    private fun drawFaces(faces: MutableList<Rect>?) {
        cameraView.overlay.clear()
        faces?.forEach {
            cameraView.overlay.add(RectangleDrawable(it))
        }
    }
}
