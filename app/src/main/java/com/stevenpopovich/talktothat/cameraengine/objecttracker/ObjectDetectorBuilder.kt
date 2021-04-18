package com.stevenpopovich.talktothat.cameraengine.objecttracker

import com.google.mlkit.vision.objects.ObjectDetection
import com.google.mlkit.vision.objects.ObjectDetector
import com.google.mlkit.vision.objects.defaults.ObjectDetectorOptions

fun getDetector(builder: ObjectDetectorOptions.Builder): ObjectDetector {
    val clientOptions = builder
        .setDetectorMode(ObjectDetectorOptions.STREAM_MODE)
        .enableMultipleObjects()
        .build()

    return ObjectDetection.getClient(clientOptions)
}
