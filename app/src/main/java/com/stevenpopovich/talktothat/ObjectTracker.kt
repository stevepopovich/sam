package com.stevenpopovich.talktothat

import android.media.Image
import com.google.mlkit.vision.objects.ObjectDetection
import com.google.mlkit.vision.objects.defaults.ObjectDetectorOptions

class ObjectTracker {
    init {
        val options = ObjectDetectorOptions.Builder()
            .setDetectorMode(ObjectDetectorOptions.STREAM_MODE)
            .enableMultipleObjects()
            .build()

        val objectDetector = ObjectDetection.getClient(options)
    }

    fun processImage(image: Image) {
        image.timestamp.toString().verboseLog()
    }
}
