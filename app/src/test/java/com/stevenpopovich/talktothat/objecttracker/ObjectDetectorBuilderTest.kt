package com.stevenpopovich.talktothat.objecttracker

import com.google.mlkit.vision.objects.ObjectDetection
import com.google.mlkit.vision.objects.ObjectDetector
import com.google.mlkit.vision.objects.defaults.ObjectDetectorOptions
import com.stevenpopovich.talktothat.testutils.relaxedMock
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockkStatic
import io.mockk.verifyOrder
import io.mockk.verifySequence
import org.junit.Assert.assertEquals
import org.junit.Test

class ObjectDetectorBuilderTest {
    @Test
    fun `we get the expected options from the builder`() {
        mockkStatic(ObjectDetection::class)

        val expectedObjectDetector: ObjectDetector = relaxedMock()

        every { ObjectDetection.getClient(any()) } returns expectedObjectDetector

        val builder: ObjectDetectorOptions.Builder = relaxedMock()
        val actualDetector = ObjectDetectorBuilder.getDetector(builder)

        verifySequence {
            ObjectDetection.getClient(
                builder
                    .setDetectorMode(ObjectDetectorOptions.STREAM_MODE)
                    .enableMultipleObjects()
                    .enableClassification()
                    .build()
            )
        }

        assertEquals(expectedObjectDetector, actualDetector)

        verifyOrder { expectedObjectDetector.equals(actualDetector) }

        confirmVerified(expectedObjectDetector, builder)
    }
}
