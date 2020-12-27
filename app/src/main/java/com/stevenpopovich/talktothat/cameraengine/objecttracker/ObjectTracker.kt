package com.stevenpopovich.talktothat.cameraengine.objecttracker

import com.google.mlkit.vision.objects.ObjectDetector
import com.google.mlkit.vision.objects.defaults.ObjectDetectorOptions
import com.otaliastudios.cameraview.frame.Frame
import com.otaliastudios.cameraview.frame.FrameProcessor
import com.stevenpopovich.talktothat.cameraengine.InputImageBuilder

class ObjectTracker(
    private val onSuccessListener: DetectedObjectSuccessListener,
    private val inputImageBuilder: InputImageBuilder = InputImageBuilder(),
    private val objectDetector: ObjectDetector = ObjectDetectorBuilder().getDetector(ObjectDetectorOptions.Builder()),
) : FrameProcessor {
    override fun process(frame: Frame) {
        val inputImage = inputImageBuilder.buildImageFromFrame(frame)
        val detectedObjects = objectDetector.process(inputImage)
        detectedObjects.addOnSuccessListener(onSuccessListener)
    }
}
