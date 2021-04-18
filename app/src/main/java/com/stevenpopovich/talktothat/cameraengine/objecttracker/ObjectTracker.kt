package com.stevenpopovich.talktothat.cameraengine.objecttracker

import com.google.mlkit.vision.objects.ObjectDetector
import com.otaliastudios.cameraview.frame.Frame
import com.otaliastudios.cameraview.frame.FrameProcessor
import com.stevenpopovich.talktothat.MainDependencyModule
import com.stevenpopovich.talktothat.cameraengine.buildImageFromFrame

class ObjectTracker(
    private val onSuccessListener: DetectedObjectSuccessListener = MainDependencyModule.onObjectDetectedSuccessListener,
    private val objectDetector: ObjectDetector = MainDependencyModule.objectDetector,
) : FrameProcessor {
    override fun process(frame: Frame) {
        val inputImage = buildImageFromFrame(frame)
        val detectedObjects = objectDetector.process(inputImage)
        detectedObjects.addOnSuccessListener(onSuccessListener)
    }
}
