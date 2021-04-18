package com.stevenpopovich.talktothat.cameraengine.facialdetection

import com.google.mlkit.common.sdkinternal.MlKitContext
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetector
import com.google.mlkit.vision.face.FaceDetectorOptions
import com.otaliastudios.cameraview.frame.Frame
import com.stevenpopovich.talktothat.MainDependencyModule
import com.stevenpopovich.talktothat.testutils.relaxedMock
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockkStatic
import io.mockk.verifySequence
import org.junit.Test

class FaceDetectionEngineTest {
    private val faceDetector: FaceDetector = relaxedMock()
    private val faceDetectionSuccessListener: FaceDetectionSuccessListener = relaxedMock()

    private val frame: Frame = relaxedMock()

    private val faceDetectionEngine: FaceDetectionEngine = FaceDetectionEngine(faceDetectionSuccessListener, faceDetector)

    @Test
    fun `face detection engine can process a frame`() {
        val byteArray = ByteArray(1)
        every { frame.getData<ByteArray>() } returns byteArray

        mockkStatic(InputImage::class)

        val inputImage: InputImage = relaxedMock()
        every {
            InputImage.fromByteArray(
                byteArray,
                frame.size.width,
                frame.size.height,
                frame.rotationToView,
                frame.format
            )
        } returns inputImage

        faceDetectionEngine.process(frame)

        verifySequence {
            frame.getData<ByteArray>()
            InputImage.fromByteArray(
                byteArray,
                frame.size.width,
                frame.size.height,
                frame.rotationToView,
                frame.format
            )
            faceDetector
                .process(inputImage)
                .addOnSuccessListener(faceDetectionSuccessListener)
        }

        confirmVerified(frame, faceDetector, faceDetectionSuccessListener, inputImage)
    }

    @Test
    fun `object tracker has optional parameters`() {
        mockkStatic(MlKitContext::class)
        mockkStatic(FaceDetection::class)
        every { getDetector(FaceDetectorOptions.Builder()) } returns relaxedMock()
        MainDependencyModule.faceDetectionSuccessListener = faceDetectionSuccessListener
        FaceDetectionEngine()
    }
}
