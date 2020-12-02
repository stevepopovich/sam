package com.stevenpopovich.talktothat.objecttracker

import com.google.mlkit.vision.objects.ObjectDetection
import com.google.mlkit.vision.objects.ObjectDetector
import com.google.mlkit.vision.objects.defaults.ObjectDetectorOptions
import com.stevenpopovich.talktothat.testutils.relaxedMock
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockkStatic
import io.mockk.verify
import io.mockk.verifySequence
import org.junit.Assert.assertEquals
import org.junit.Test

class ObjectDetectorBuilderTest {
    @Test
    fun `we get the expected options from the builder`() {
        mockkStatic(ObjectDetection::class)

        val expectedObjectDetector: ObjectDetector = relaxedMock()

        val builder: ObjectDetectorOptions.Builder = relaxedMock()

        every {
            ObjectDetection.getClient(
                builder
                    .setDetectorMode(ObjectDetectorOptions.STREAM_MODE)
                    .enableMultipleObjects()
                    .build()
            )
        } returns expectedObjectDetector

        val actualDetector = ObjectDetectorBuilder().getDetector(builder)

        verifySequence {
            val clientOptions = builder
                .setDetectorMode(ObjectDetectorOptions.STREAM_MODE)
                .enableMultipleObjects()
                .build()
            ObjectDetection.getClient(clientOptions)
        }

        assertEquals(expectedObjectDetector, actualDetector)
        verify { expectedObjectDetector.equals(actualDetector) }

        confirmVerified(expectedObjectDetector, builder)
    }
}
