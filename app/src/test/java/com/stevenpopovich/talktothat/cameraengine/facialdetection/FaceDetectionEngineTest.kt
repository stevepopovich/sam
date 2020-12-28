package com.stevenpopovich.talktothat.cameraengine.facialdetection

import com.google.mlkit.vision.face.FaceDetector
import com.otaliastudios.cameraview.frame.Frame
import com.stevenpopovich.talktothat.cameraengine.InputImageBuilder
import com.stevenpopovich.talktothat.testutils.relaxedMock
import io.mockk.confirmVerified
import io.mockk.verifySequence
import org.junit.Assert
import org.junit.Test

class FaceDetectionEngineTest {
    private val inputImageBuilder: InputImageBuilder = relaxedMock()
    private val faceDetector: FaceDetector = relaxedMock()
    private val faceDetectionSuccessListener: FaceDetectionSuccessListener = relaxedMock()

    private val frame: Frame = relaxedMock()

    private val faceDetectionEngine: FaceDetectionEngine = FaceDetectionEngine(faceDetectionSuccessListener, inputImageBuilder, faceDetector)

    @Test
    fun `face detection engine can process a frame`() {
        faceDetectionEngine.process(frame)

        verifySequence {
            val inputImage = inputImageBuilder.buildImageFromFrame(frame)
            faceDetector
                .process(inputImage)
                .addOnSuccessListener(faceDetectionSuccessListener)
        }

        confirmVerified(frame, inputImageBuilder, faceDetector, faceDetectionSuccessListener)
    }

    @Test
    fun `object tracker has optional parameters`() {
        try {
            FaceDetectionEngine(faceDetectionSuccessListener)
        } catch (err: Exception) {
            Assert.assertTrue(err is IllegalStateException)
        }
    }
}
