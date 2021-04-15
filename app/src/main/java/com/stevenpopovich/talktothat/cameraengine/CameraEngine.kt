package com.stevenpopovich.talktothat.cameraengine

import com.otaliastudios.cameraview.CameraView
import com.otaliastudios.cameraview.controls.Engine
import com.otaliastudios.cameraview.controls.Facing
import com.stevenpopovich.talktothat.MainDependencyModule
import com.stevenpopovich.talktothat.cameraengine.facialdetection.FaceDetectionEngine
import com.stevenpopovich.talktothat.cameraengine.objecttracker.ObjectTracker

class CameraEngine(
    private val camera: CameraView = MainDependencyModule.camera,
    private val objectTracker: ObjectTracker = MainDependencyModule.objectTracker,
    private val faceDetectionEngine: FaceDetectionEngine = MainDependencyModule.faceDetectionEngine
) {
    fun start() {
        camera.facing = Facing.BACK
        camera.previewFrameRate = 60.0f
        camera.previewFrameRateExact = true
        camera.engine = Engine.CAMERA2

        camera.clearFrameProcessors()

        // Turned off for performance reasons
//        camera.addFrameProcessor(objectTracker)
        camera.addFrameProcessor(faceDetectionEngine)
    }
}
