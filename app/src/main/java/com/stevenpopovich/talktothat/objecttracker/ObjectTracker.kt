package com.stevenpopovich.talktothat.objecttracker

import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.objects.ObjectDetection
import com.google.mlkit.vision.objects.ObjectDetector
import com.google.mlkit.vision.objects.defaults.ObjectDetectorOptions
import com.otaliastudios.cameraview.CameraView
import com.otaliastudios.cameraview.frame.Frame
import com.otaliastudios.cameraview.frame.FrameProcessor

class ObjectTracker(private val cameraView: CameraView) : FrameProcessor {
    private val objectDetector: ObjectDetector

    init {
        val options = ObjectDetectorOptions.Builder()
            .setDetectorMode(ObjectDetectorOptions.STREAM_MODE)
            .enableMultipleObjects()
            .enableClassification()
            .build()

        objectDetector = ObjectDetection.getClient(options)
    }

    override fun process(frame: Frame) {
        val image = frame.getData<ByteArray>()
        val inputImage = InputImage.fromByteArray(
            image,
            frame.size.width,
            frame.size.height,
            frame.rotationToView,
            frame.format
        )

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
