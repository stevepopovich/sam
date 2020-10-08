package com.stevenpopovich.talktothat.objecttracker

import com.google.mlkit.vision.objects.ObjectDetector
import com.otaliastudios.cameraview.frame.Frame
import com.stevenpopovich.talktothat.testutils.relaxedMock
import io.mockk.confirmVerified
import io.mockk.verifySequence
import org.junit.Assert.assertTrue
import org.junit.Test

class ObjectTrackerTest {
    private val inputImageBuilder: InputImageBuilder = relaxedMock()
    private val objectDetector: ObjectDetector = relaxedMock()
    private val detectedObjectSuccessListener: DetectedObjectSuccessListener = relaxedMock()

    private val frame: Frame = relaxedMock()

    private val objectTracker: ObjectTracker = ObjectTracker(detectedObjectSuccessListener, inputImageBuilder, objectDetector)

    @Test
    fun `object tracker can process frame`() {
        objectTracker.process(frame)

        verifySequence {
            val inputImage = inputImageBuilder.buildImageFromFrame(frame)
            val detectedObjects = objectDetector.process(inputImage)
            detectedObjects.addOnSuccessListener(detectedObjectSuccessListener)
        }

        confirmVerified(frame, inputImageBuilder, objectDetector, detectedObjectSuccessListener)
    }

    @Test
    fun `object tracker has optional parameters`() {
        try {
            ObjectTracker(detectedObjectSuccessListener)
        } catch (err: Exception) {
            assertTrue(err is IllegalStateException)
        }
    }
}
