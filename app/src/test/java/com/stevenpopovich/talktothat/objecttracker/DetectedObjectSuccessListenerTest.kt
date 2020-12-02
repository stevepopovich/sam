package com.stevenpopovich.talktothat.objecttracker

import android.view.ViewGroupOverlay
import com.google.mlkit.vision.objects.DetectedObject
import com.otaliastudios.cameraview.CameraView
import com.stevenpopovich.talktothat.testutils.relaxedMock
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.verifySequence
import org.junit.Test

class DetectedObjectSuccessListenerTest {
    @Test
    fun `test we can get detected object success listener`() {
        val cameraView: CameraView = relaxedMock()
        val object1: DetectedObject = relaxedMock()
        val object2: DetectedObject = relaxedMock()

        val overlay: ViewGroupOverlay = relaxedMock()
        val objects: MutableList<DetectedObject> = mutableListOf(object1, object2)

        every { object1.boundingBox } returns relaxedMock()
        every { object2.boundingBox } returns relaxedMock()

        every { cameraView.overlay } returns overlay

        every { overlay.clear() } returns Unit

        val listener = DetectedObjectSuccessListener(cameraView)

        listener.onSuccess(objects)

        verifySequence {
            cameraView.overlay
            cameraView.overlay
            object1.boundingBox
            cameraView.overlay
            object2.boundingBox
        }

        confirmVerified(cameraView, object1, object2)
    }
}
