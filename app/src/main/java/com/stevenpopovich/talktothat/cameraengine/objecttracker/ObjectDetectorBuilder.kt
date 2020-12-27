package com.stevenpopovich.talktothat.objecttracker

import com.google.mlkit.vision.objects.ObjectDetection
import com.google.mlkit.vision.objects.ObjectDetector
import com.google.mlkit.vision.objects.defaults.ObjectDetectorOptions

class ObjectDetectorBuilder {
    fun getDetector(builder: ObjectDetectorOptions.Builder): ObjectDetector {
        val clientOptions = builder
            .setDetectorMode(ObjectDetectorOptions.STREAM_MODE)
            .enableMultipleObjects()
            .build()

        return ObjectDetection.getClient(clientOptions)
    }
}
