package com.stevenpopovich.talktothat

import android.content.Intent
import com.stevenpopovich.talktothat.cameraengine.CameraEngine
import com.stevenpopovich.talktothat.speechrecognition.ContinuousSpeechRecognitionListener
import com.stevenpopovich.talktothat.speechrecognition.ContinuousSpeechRecognizer

class AppEngine {
    fun start(
        continuousSpeechRecognizer: ContinuousSpeechRecognizer = MainDependencyModule.continuousSpeechRecognizer,
        cameraEngine: CameraEngine = MainDependencyModule.cameraEngine,
        intent: Intent = Intent(),
        // This looks weird - It does this because the listener needs to know how to restart itself
        recognitionListener: ContinuousSpeechRecognitionListener = ContinuousSpeechRecognitionListener() { continuousSpeechRecognizer.startListening(intent, it) }
    ) {
        continuousSpeechRecognizer.startListening(
            intent,
            recognitionListener
        )

        cameraEngine.start()
    }
}
