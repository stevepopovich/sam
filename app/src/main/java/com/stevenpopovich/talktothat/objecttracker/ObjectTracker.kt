package com.stevenpopovich.talktothat.objecttracker

import com.google.mlkit.vision.objects.ObjectDetector
import com.google.mlkit.vision.objects.defaults.ObjectDetectorOptions
import com.otaliastudios.cameraview.CameraView
import com.otaliastudios.cameraview.frame.Frame
import com.otaliastudios.cameraview.frame.FrameProcessor

class ObjectTracker(
    private val cameraView: CameraView,
    private val objectDetector: ObjectDetector = ObjectDetectorBuilder.getDetector(ObjectDetectorOptions.Builder()),
    private val inputImageBuilder: InputImageBuilder = InputImageBuilder()
) : FrameProcessor {
    override fun process(frame: Frame) {
        val inputImage = inputImageBuilder.buildImageFromFrame(frame)

        val detectedObjects = objectDetector.process(inputImage)
        detectedObjects.addOnSuccessListener {
            cameraView.overlay.clear()
            it.forEach {
                if (it.labels.map { it.text }.contains("Fashion good")) {
                    cameraView.overlay.add(RectangleDrawable(it.boundingBox))
                }
            }
        }
    }
}
