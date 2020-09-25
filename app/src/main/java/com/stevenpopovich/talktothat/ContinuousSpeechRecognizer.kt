package com.stevenpopovich.talktothat

import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer

typealias SpeechResultBusinessLogic = (results: Bundle?) -> Unit
typealias SpeechRecognizerRestartLogic = (speechRecognitionListener: ContinuousSpeechRecognitionListener) -> Unit

class ContinuousSpeechRecognizer {
    companion object {
        fun start(speechRecognizer: SpeechRecognizer, speechResultsBusinessLogicEngine: SpeechResultsBusinessLogicEngine) {
            val continuousSpeechRecognizer = ContinuousSpeechRecognizer()
            val recognitionListener = ContinuousSpeechRecognitionListener(
                speechResultsBusinessLogicEngine::onSpeechResults
            ) { continuousSpeechRecognizer.startListening(speechRecognizer, it, Intent()) }

            continuousSpeechRecognizer.startListening(
                speechRecognizer,
                recognitionListener,
                Intent()
            )
        }
    }

    fun startListening(speechRecognizer: SpeechRecognizer, recognitionListener: ContinuousSpeechRecognitionListener, intent: Intent) {
        speechRecognizer.destroy()
        speechRecognizer.setRecognitionListener(recognitionListener)

        intent.action = RecognizerIntent.ACTION_RECOGNIZE_SPEECH
        intent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)

        speechRecognizer.startListening(intent)
    }
}
