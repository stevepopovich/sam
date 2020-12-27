package com.stevenpopovich.talktothat.cameraengine

import com.google.mlkit.vision.common.InputImage
import com.otaliastudios.cameraview.frame.Frame
import com.otaliastudios.cameraview.size.Size
import com.stevenpopovich.talktothat.testutils.relaxedMock
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockkStatic
import io.mockk.verifySequence
import org.junit.Test

class InputImageBuilderTest {
    @Test
    fun `we can get an image from the image builder`() {
        mockkStatic(InputImage::class)

        val frame: Frame = relaxedMock()
        val byteArray = ByteArray(2)
        val size: Size = relaxedMock()

        every { frame.size } returns size
        every { frame.size.width } returns 10
        every { frame.size.height } returns 10
        every { size.width } returns 10
        every { size.height } returns 10
        every { frame.rotationToView } returns 0
        every { frame.format } returns InputImage.IMAGE_FORMAT_NV21
        every { frame.getData<ByteArray>() } returns byteArray

        every {
            InputImage.fromByteArray(
                any(),
                any(),
                any(),
                any(),
                any()
            )
        } returns relaxedMock()

        val inputImageBuilder = InputImageBuilder()
        inputImageBuilder.buildImageFromFrame(frame)

        verifySequence {
            frame.getData<ByteArray>()
            frame.size
            frame.size
            frame.rotationToView
            frame.format
            InputImage.fromByteArray(
                byteArray,
                10,
                10,
                0,
                17
            )
        }

        confirmVerified(frame, size)
    }
}
