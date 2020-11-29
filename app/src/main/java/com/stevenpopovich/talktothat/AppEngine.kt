package com.stevenpopovich.talktothat

import android.content.Intent
import com.stevenpopovich.talktothat.speechrecognition.ContinuousSpeechRecognitionListener
import com.stevenpopovich.talktothat.speechrecognition.ContinuousSpeechRecognizer
import com.stevenpopovich.talktothat.speechrecognition.SpeechResultsBusinessLogicEngine

class AppEngine {
    fun start(
        continuousSpeechRecognizer: ContinuousSpeechRecognizer,
        logicEngine: SpeechResultsBusinessLogicEngine,
        intent: Intent = Intent(),
        recognitionListener: ContinuousSpeechRecognitionListener = ContinuousSpeechRecognitionListener(
            logicEngine::onSpeechResults
        ) { continuousSpeechRecognizer.startListening(intent, it) }
    ) {
        continuousSpeechRecognizer.startListening(
            intent,
            recognitionListener
        )
    }
}
