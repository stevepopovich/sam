package com.stevenpopovich.talktothat.cameraengine

import com.otaliastudios.cameraview.CameraView
import com.otaliastudios.cameraview.controls.Engine
import com.otaliastudios.cameraview.controls.Facing
import com.stevenpopovich.talktothat.MainDependencyModule
import com.stevenpopovich.talktothat.testutils.relaxedMock
import io.mockk.confirmVerified
import io.mockk.verifyOrder
import org.junit.Test

class CameraEngineTest {
    private val camera: CameraView = relaxedMock()

    private val cameraEngine = CameraEngine(camera)

    @Test
    fun testCameraEngineCanStart() {
        cameraEngine.start()

        verifyOrder {
            camera.facing = Facing.BACK
            camera.previewFrameRate = 30.0f
            camera.previewFrameRateExact = true
            camera.engine = Engine.CAMERA2
            camera.clearFrameProcessors()
        }

        confirmVerified(camera)
    }

    @Test
    fun `test optional parameters`() {
        MainDependencyModule.camera = relaxedMock()

        CameraEngine()
    }
}
