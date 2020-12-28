package com.stevenpopovich.talktothat.cameraengine.facialdetection

import com.google.mlkit.vision.face.FaceDetector
import com.google.mlkit.vision.face.FaceDetectorOptions
import com.otaliastudios.cameraview.frame.Frame
import com.otaliastudios.cameraview.frame.FrameProcessor
import com.stevenpopovich.talktothat.cameraengine.InputImageBuilder

class FaceDetectionEngine(
    private val faceDetectionSuccessListener: FaceDetectionSuccessListener,
    private val inputImageBuilder: InputImageBuilder = InputImageBuilder(),
    private val faceDetector: FaceDetector = FaceDetectorBuilder().getDetector(
        FaceDetectorOptions.Builder()
    )
) : FrameProcessor {
    override fun process(frame: Frame) {
        val inputImage = inputImageBuilder.buildImageFromFrame(frame)
        faceDetector
            .process(inputImage)
            .addOnSuccessListener(faceDetectionSuccessListener)
    }
}
