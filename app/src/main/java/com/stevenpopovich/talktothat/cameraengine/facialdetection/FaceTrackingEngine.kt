package com.stevenpopovich.talktothat.cameraengine.facialdetection

import com.google.mlkit.vision.face.FaceDetector
import com.google.mlkit.vision.face.FaceDetectorOptions
import com.otaliastudios.cameraview.frame.Frame
import com.otaliastudios.cameraview.frame.FrameProcessor
import com.stevenpopovich.talktothat.MainDependencyModule
import com.stevenpopovich.talktothat.cameraengine.buildImageFromFrame

class FaceTrackingEngine(
    private val faceDetectionSuccessListener: FaceDetectionSuccessListener = MainDependencyModule.faceDetectionSuccessListener,
    private val faceDetector: FaceDetector = getDetector(FaceDetectorOptions.Builder())
) : FrameProcessor {
    override fun process(frame: Frame) {
        val inputImage = buildImageFromFrame(frame)
        faceDetector
            .process(inputImage)
            .addOnSuccessListener(faceDetectionSuccessListener)
    }
}
