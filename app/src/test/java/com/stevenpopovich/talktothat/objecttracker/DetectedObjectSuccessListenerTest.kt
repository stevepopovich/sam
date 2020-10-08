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

        val label1: DetectedObject.Label = relaxedMock()
        every { label1.text } returns "Fashion good"

        val label2: DetectedObject.Label = relaxedMock()
        every { label2.text } returns "not a fashion good"

        every { object1.labels } returns listOf(label1)
        every { object2.labels } returns listOf(label2)

        every { cameraView.overlay } returns overlay

        every { overlay.clear() } returns Unit

        val listener = DetectedObjectSuccessListener(cameraView)

        listener.onSuccess(objects)

        verifySequence {
            cameraView.overlay
            object1.labels
            label1.text
            cameraView.overlay
            object1.boundingBox
            object2.labels
            label2.text
        }

        confirmVerified(cameraView, object1, object2, label1, label2)
    }
}
