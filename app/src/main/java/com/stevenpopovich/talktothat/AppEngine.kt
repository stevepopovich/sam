package com.stevenpopovich.talktothat

import android.content.Intent
import com.stevenpopovich.talktothat.cameraengine.CameraEngine
import com.stevenpopovich.talktothat.speechrecognition.ContinuousSpeechRecognitionListener
import com.stevenpopovich.talktothat.speechrecognition.ContinuousSpeechRecognizer
import com.stevenpopovich.talktothat.speechrecognition.SpeechResultsBusinessLogicEngine

class AppEngine {
    fun start(
        continuousSpeechRecognizer: ContinuousSpeechRecognizer = MainDependencyModule.continuousSpeechRecognizer,
        logicEngine: SpeechResultsBusinessLogicEngine = MainDependencyModule.speechResultsBusinessLogicEngine,
        cameraEngine: CameraEngine = MainDependencyModule.cameraEngine,
        intent: Intent = Intent(),
        // This looks weird - It does this because it needs to know how to restart itself, o
        recognitionListener: ContinuousSpeechRecognitionListener = ContinuousSpeechRecognitionListener(
            logicEngine::onSpeechResults
        ) { continuousSpeechRecognizer.startListening(intent, it) }
    ) {
        continuousSpeechRecognizer.startListening(
            intent,
            recognitionListener
        )

        cameraEngine.start()
    }
}
