package com.stevenpopovich.talktothat.objecttracker

import com.google.mlkit.vision.common.InputImage
import com.otaliastudios.cameraview.frame.Frame
import com.stevenpopovich.talktothat.testutils.relaxedMock
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.verifySequence
import org.junit.Test

class InputImageBuilderTest {
    private val inputImageBuilder = InputImageBuilder()

    @Test
    fun `we can get an image from the image builder`() {
        val frame: Frame = relaxedMock()
        val byteArray: ByteArray = relaxedMock()
        every { frame.getData<ByteArray>() } returns byteArray

        inputImageBuilder.buildImageFromFrame(frame)

        verifySequence {
            frame.getData<ByteArray>()
            InputImage.fromByteArray(
                any(),
                frame.size.width,
                frame.size.height,
                frame.rotationToView,
                frame.format
            )
        }

        confirmVerified(frame)
    }
}
