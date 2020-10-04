package com.stevenpopovich.talktothat

import android.content.Intent

class AppEngine {
    fun start(
        continuousSpeechRecognizer: ContinuousSpeechRecognizer,
        logicEngine: SpeechResultsBusinessLogicEngine,
        cameraEngine: CameraEngine
    ) {
        val recognitionListener = ContinuousSpeechRecognitionListener(
            logicEngine::onSpeechResults
        ) { continuousSpeechRecognizer.startListening(Intent(), it) }

        continuousSpeechRecognizer.startListening(
            Intent(),
            recognitionListener
        )

        cameraEngine.start()
    }
}
