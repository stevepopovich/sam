package com.stevenpopovich.talktothat.cameraengine.objecttracker

import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.objects.DetectedObject
import com.google.mlkit.vision.objects.ObjectDetector
import com.otaliastudios.cameraview.frame.Frame
import com.stevenpopovich.talktothat.MainDependencyModule
import com.stevenpopovich.talktothat.cameraengine.buildImageFromFrame
import com.stevenpopovich.talktothat.testutils.relaxedMock
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockkStatic
import io.mockk.verifySequence
import org.junit.Test

class ObjectTrackerTest {
    private val objectDetector: ObjectDetector = relaxedMock()
    private val detectedObjectSuccessListener: DetectedObjectSuccessListener = relaxedMock()

    private val frame: Frame = relaxedMock()

    private val objectTracker: ObjectTracker = ObjectTracker(detectedObjectSuccessListener, objectDetector)

    @Test
    fun `object tracker can process frame`() {
        val byteArray = ByteArray(0)
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

        val detectedObjects: Task<List<DetectedObject>> = relaxedMock()
        every { objectDetector.process(inputImage) } returns detectedObjects

        objectTracker.process(frame)

        verifySequence {
            buildImageFromFrame(frame)
            objectDetector.process(inputImage)
            detectedObjects.addOnSuccessListener(detectedObjectSuccessListener as OnSuccessListener<in List<DetectedObject>>)
        }

        confirmVerified(frame, objectDetector, detectedObjectSuccessListener, inputImage)
    }

    @Test
    fun `object tracker has optional parameters`() {
        MainDependencyModule.onObjectDetectedSuccessListener = relaxedMock()
        MainDependencyModule.objectDetector = relaxedMock()
        ObjectTracker()
    }
}
