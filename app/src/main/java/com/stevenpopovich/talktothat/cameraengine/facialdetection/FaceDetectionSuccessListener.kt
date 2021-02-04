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
import com.stevenpopovich.talktothat.verboseLog

const val HORIZONTAL_KP = 0.05
const val HORIZONTAL_KI = 0.05
const val HORIZONTAL_KD = 0.05
val HORIZONTAL_CONTROLLER_DIRECTION = ControllerDirection.DIRECT

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
        horizontalPid.setOutputLimits(0.0, 180.0)
        horizontalProcess.setpoint = cameraView.width.toDouble() / 2.0
    }

    override fun onSuccess(faces: MutableList<Face>?) {
        if (faces?.isNullOrEmpty() != false) {
            cameraView.overlay.clear()
            serialPortInterface?.let { serialPortInterface ->
                arduinoInterface.writeStringToSerialPort(serialPortInterface, 0.toString())
            }
        } else {
            faces?.firstOrNull()?.let { face ->
                horizontalProcess.input = face.boundingBox.centerX().toDouble()
                horizontalPid.compute()

                val correctOutput = if (face.boundingBox.centerX() < horizontalProcess.setpoint)
                    -horizontalProcess.output
                else
                    horizontalProcess.output

                correctOutput.verboseLog()
                drawFaces(mutableListOf(face.boundingBox))
//                getSpinSpeedByXDistance(movedBoundingBox).toString()
                serialPortInterface?.let { serialPortInterface ->
                    arduinoInterface.writeStringToSerialPort(
                        serialPortInterface,
                        correctOutput.toString()
                    )
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
