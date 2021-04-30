package com.stevenpopovich.talktothat.cameraengine

import com.otaliastudios.cameraview.CameraView
import com.otaliastudios.cameraview.controls.Engine
import com.otaliastudios.cameraview.controls.Facing
import com.stevenpopovich.talktothat.MainDependencyModule

class CameraEngine(
    private val camera: CameraView = MainDependencyModule.camera,
) {
    fun start() {
        camera.facing = Facing.BACK
        camera.previewFrameRate = 30.0f // this is the max
        camera.previewFrameRateExact = true
        camera.engine = Engine.CAMERA2

        camera.clearFrameProcessors()
    }
}
