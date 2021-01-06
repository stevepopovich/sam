package com.stevenpopovich.talktothat.cameraengine.facialdetection

import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.hardware.usb.UsbManager
import android.view.ViewGroupOverlay
import android.widget.TextView
import com.google.mlkit.vision.face.Face
import com.otaliastudios.cameraview.CameraView
import com.stevenpopovich.talktothat.MainFragment
import com.stevenpopovich.talktothat.cameraengine.moveXOneThirdRight
import com.stevenpopovich.talktothat.testutils.relaxedMock
import com.stevenpopovich.talktothat.usbinterfacing.ArduinoInterface
import com.stevenpopovich.talktothat.usbinterfacing.SerialPortInterface
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.slot
import io.mockk.verify
import io.mockk.verifySequence
import org.junit.Before
import org.junit.Test

class FaceDetectionSuccessListenerTest {
    private lateinit var cameraView: CameraView
    private lateinit var usbManager: UsbManager
    private lateinit var debugTextView: TextView
    private lateinit var mainFragment: MainFragment
    private lateinit var arduinoInterface: ArduinoInterface
    private lateinit var serialPortInterface: SerialPortInterface

    private lateinit var overlay: ViewGroupOverlay

    private lateinit var listener: FaceDetectionSuccessListener

    private val cameraViewWidth = 400

    @Before
    fun setup() {
        cameraView = relaxedMock()
        usbManager = relaxedMock()
        debugTextView = relaxedMock()
        mainFragment = relaxedMock()
        arduinoInterface = relaxedMock()
        serialPortInterface = relaxedMock()

        overlay = relaxedMock()

        listener = FaceDetectionSuccessListener(
            cameraView,
            usbManager,
            debugTextView,
            mainFragment,
            arduinoInterface,
            serialPortInterface
        )

        every { cameraView.overlay } returns overlay
        every { cameraView.width } returns cameraViewWidth
        every { overlay.clear() } returns Unit
    }

    @Test
    fun `test that if we have no detected faces, we stop moving`() {
        listener.onSuccess(null)

        verifySequence {
            cameraView.overlay
            overlay.clear()
            serialPortInterface?.let { serialPortInterface ->
                arduinoInterface.writeStringToSerialPort(serialPortInterface, 0.toString())
            }
        }

        confirmVerified(
            cameraView,
            usbManager,
            debugTextView,
            mainFragment,
            arduinoInterface,
            serialPortInterface
        )
    }

    @Test
    fun `test we draw the first detected face`() {
        val face1: Face = relaxedMock()
        val face2: Face = relaxedMock()

        val boundingBox1: Rect = relaxedMock()
        val movedBoundingBox: Rect = relaxedMock()

        val faces: MutableList<Face> = mutableListOf(face1, face2)

        every { face1.boundingBox } returns boundingBox1

        every { boundingBox1.moveXOneThirdRight() } returns movedBoundingBox

        val overlayDrawableSlot = slot<Drawable>()
        every { overlay.add(capture(overlayDrawableSlot)) } returns Unit

        listener.onSuccess(faces)

        verifySequence {
            boundingBox1.moveXOneThirdRight()
            cameraView.overlay
            overlay.clear()
            cameraView.overlay
            overlay.add(any<Drawable>())
            cameraView.width
            boundingBox1.centerX()
        }

        confirmVerified(
            cameraView
        )
    }

    @Test
    fun `test that we move to follow the first found face`() {
        val face1: Face = relaxedMock()
        val face2: Face = relaxedMock()

        val boundingBox1: Rect = relaxedMock()
        val movedBoundingBox: Rect = relaxedMock()

        val faces: MutableList<Face> = mutableListOf(face1, face2)

        every { face1.boundingBox } returns boundingBox1

        every { boundingBox1.moveXOneThirdRight() } returns movedBoundingBox

        every { boundingBox1.centerX() } returns 300

        listener.onSuccess(faces)

        verify {
            arduinoInterface.writeStringToSerialPort(
                serialPortInterface,
                "70"
            )
        }

        confirmVerified(
            arduinoInterface,
            serialPortInterface,
        )
    }

    @Test
    fun `test that we dont move to follow the first found face if its close to the center`() {
        val face1: Face = relaxedMock()
        val face2: Face = relaxedMock()

        val boundingBox1: Rect = relaxedMock()
        val movedBoundingBox: Rect = relaxedMock()

        val faces: MutableList<Face> = mutableListOf(face1, face2)

        every { face1.boundingBox } returns boundingBox1

        every { boundingBox1.moveXOneThirdRight() } returns movedBoundingBox

        every { boundingBox1.centerX() } returns 210

        listener.onSuccess(faces)

        verify {
            arduinoInterface.writeStringToSerialPort(
                serialPortInterface,
                "0"
            )
        }

        confirmVerified(
            arduinoInterface,
            serialPortInterface,
        )
    }

    @Test
    fun `test that we have optional parameters`() {
        FaceDetectionSuccessListener(
            cameraView,
            usbManager,
            debugTextView,
            mainFragment
        )
    }
}
