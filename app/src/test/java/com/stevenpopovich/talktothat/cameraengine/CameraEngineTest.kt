package com.stevenpopovich.talktothat.cameraengine

import com.otaliastudios.cameraview.CameraView
import com.otaliastudios.cameraview.controls.Engine
import com.otaliastudios.cameraview.controls.Facing
import com.stevenpopovich.talktothat.cameraengine.facialdetection.FaceDetectionEngine
import com.stevenpopovich.talktothat.cameraengine.objecttracker.ObjectTracker
import com.stevenpopovich.talktothat.testutils.relaxedMock
import io.mockk.confirmVerified
import io.mockk.verifyOrder
import org.junit.Test

class CameraEngineTest {
    private val camera: CameraView = relaxedMock()
    private val objectTracker: ObjectTracker = relaxedMock()
    private val faceDetectionEngine: FaceDetectionEngine = relaxedMock()

    private val cameraEngine = CameraEngine(camera, objectTracker, faceDetectionEngine)

    @Test
    fun testCameraEngineCanStart() {
        cameraEngine.start()

        verifyOrder {
            camera.facing = Facing.BACK
            camera.previewFrameRate = 30f
            camera.previewFrameRateExact = true
            camera.engine = Engine.CAMERA2

//            camera.addFrameProcessor(objectTracker)
            camera.addFrameProcessor(faceDetectionEngine)
        }

        confirmVerified(camera, objectTracker)
    }
}
