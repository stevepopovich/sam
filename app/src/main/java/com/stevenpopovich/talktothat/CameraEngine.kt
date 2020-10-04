package com.stevenpopovich.talktothat

import com.otaliastudios.cameraview.CameraView
import com.otaliastudios.cameraview.controls.Engine
import com.otaliastudios.cameraview.controls.Facing

class CameraEngine(
    private val camera: CameraView,
    private val objectTracker: ObjectTracker
) {
    fun start() {
        camera.facing = Facing.BACK
        camera.previewFrameRate = 30f
        camera.previewFrameRateExact = true
        camera.engine = Engine.CAMERA2

        camera.addFrameProcessor {
            objectTracker.processImage(it)
        }
    }
}
