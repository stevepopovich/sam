package com.stevenpopovich.talktothat.cameraengine.facialdetection

import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetector
import com.google.mlkit.vision.face.FaceDetectorOptions

class FaceDetectorBuilder {
    fun getDetector(builder: FaceDetectorOptions.Builder): FaceDetector {
        return FaceDetection.getClient(
            builder
                .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_FAST)
                .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_NONE)
                .build()
        )
    }
}
