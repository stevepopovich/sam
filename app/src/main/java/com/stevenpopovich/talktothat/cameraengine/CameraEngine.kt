package com.stevenpopovich.talktothat.cameraengine

import com.otaliastudios.cameraview.CameraView
import com.otaliastudios.cameraview.controls.Engine
import com.otaliastudios.cameraview.controls.Facing
import com.stevenpopovich.talktothat.cameraengine.facialdetection.FaceDetectionEngine
import com.stevenpopovich.talktothat.cameraengine.objecttracker.ObjectTracker

class CameraEngine(
    private val camera: CameraView,
    private val objectTracker: ObjectTracker,
    private val faceDetectionEngine: FaceDetectionEngine
) {
    fun start() {
        camera.facing = Facing.BACK
        camera.previewFrameRate = 33.3f
        camera.previewFrameRateExact = true
        camera.engine = Engine.CAMERA2

//        camera.addFrameProcessor(objectTracker)
        camera.addFrameProcessor(faceDetectionEngine)
    }
}
