package com.stevenpopovich.talktothat.objecttracker

import com.google.mlkit.vision.objects.ObjectDetection
import com.google.mlkit.vision.objects.ObjectDetector
import com.google.mlkit.vision.objects.defaults.ObjectDetectorOptions

class ObjectDetectorBuilder {
    companion object {
        fun getDetector(builder: ObjectDetectorOptions.Builder): ObjectDetector =
            ObjectDetection.getClient(
                builder
                    .setDetectorMode(ObjectDetectorOptions.STREAM_MODE)
                    .enableMultipleObjects()
                    .enableClassification()
                    .build()
            )
    }
}
