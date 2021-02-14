package com.stevenpopovich.talktothat.cameraengine.facialdetection

import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetector
import com.google.mlkit.vision.face.FaceDetectorOptions
import com.stevenpopovich.talktothat.testutils.relaxedMock
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockkStatic
import io.mockk.verify
import io.mockk.verifySequence
import org.junit.Assert
import org.junit.Test

class FaceDetectorBuilderTest {
    @Test
    fun `we get the expected options from the builder`() {
        mockkStatic(FaceDetection::class)

        val expectedFaceDetector: FaceDetector = relaxedMock()

        val builder: FaceDetectorOptions.Builder = relaxedMock()

        every {
            FaceDetection.getClient(
                builder
                    .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_FAST)
                    .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_NONE)
                    .build()
            )
        } returns expectedFaceDetector

        val actualDetector = FaceDetectorBuilder().getDetector(builder)

        verifySequence {
            FaceDetection.getClient(
                builder
                    .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_FAST)
                    .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_NONE)
                    .build()
            )
        }

        Assert.assertEquals(expectedFaceDetector, actualDetector)
        verify { expectedFaceDetector.equals(actualDetector) }

        confirmVerified(expectedFaceDetector, builder)
    }
}
