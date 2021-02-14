package com.stevenpopovich.talktothat.cameraengine.facialdetection

import android.graphics.Rect
import android.hardware.usb.UsbManager
import android.widget.TextView
import com.google.android.gms.tasks.OnSuccessListener
import com.google.mlkit.vision.face.Face
import com.otaliastudios.cameraview.CameraView
import com.stevenpopovich.talktothat.MainFragment
import com.stevenpopovich.talktothat.cameraengine.RectangleDrawable
import com.stevenpopovich.talktothat.usbinterfacing.ArduinoInterface
import com.stevenpopovich.talktothat.usbinterfacing.SerialPortInterface
import com.stevenpopovich.talktothat.usbinterfacing.SerialPortInterfaceBuilder
import com.stevenpopovich.talktothat.usbinterfacing.SerialPortReader
import kotlin.math.absoluteValue

const val HORIZONTAL_KP = 0.170 // this makes it more agressive on change
const val HORIZONTAL_KI = 0.010 // this makes it miss by an offset when we go higher
const val HORIZONTAL_KD = 0.030 // making this higher makes it oscillate
val HORIZONTAL_CONTROLLER_DIRECTION = ControllerDirection.REVERSE

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
        ),
    private val horizontalProcess: Process = FaceTrackingProcess(0.0, 0.0, 0.0),
    private val horizontalPid: PID = PID(
        horizontalProcess,
        HORIZONTAL_KP,
        HORIZONTAL_KI,
        HORIZONTAL_KD,
        HORIZONTAL_CONTROLLER_DIRECTION
    )
) : OnSuccessListener<MutableList<Face>> {

    init {
        horizontalPid.setMode(ControllerMode.AUTOMATIC)
        horizontalPid.setOutputLimits(-150.0, 150.0)
        horizontalProcess.setpoint = cameraView.width.toDouble() / 2.0
    }

    override fun onSuccess(faces: MutableList<Face>?) {
        if (faces?.isNullOrEmpty() == true) {
            cameraView.overlay.clear()
            serialPortInterface?.let { serialPortInterface ->
                arduinoInterface.writeStringToSerialPort(serialPortInterface, 0.toString())
            }
        } else {
            faces?.firstOrNull()?.let { face ->
                horizontalProcess.input = face.boundingBox.centerX().toDouble()
                horizontalPid.compute()

                drawFaces(mutableListOf(face.boundingBox))
                if (horizontalProcess.output.absoluteValue > 16) {
                    serialPortInterface?.let { serialPortInterface ->
                        arduinoInterface.writeStringToSerialPort(
                            serialPortInterface,
                            horizontalProcess.output.toString()
                        )
                    }
                } else {
                    serialPortInterface?.let { serialPortInterface ->
                        arduinoInterface.writeStringToSerialPort(serialPortInterface, 0.toString())
                    }
                }
            }
        }
    }

    private fun drawFaces(faces: MutableList<Rect>?) {
        cameraView.overlay.clear()
        faces?.forEach {
            cameraView.overlay.add(RectangleDrawable(it))
        }
    }
}
