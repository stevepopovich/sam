package com.stevenpopovich.talktothat.speechrecognition

import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer

typealias SpeechResultBusinessLogic = (results: Bundle?) -> Unit
typealias SpeechRecognizerRestartLogic = (speechRecognitionListener: ContinuousSpeechRecognitionListener) -> Unit

class ContinuousSpeechRecognizer(
    private val speechRecognizer: SpeechRecognizer
) {
    fun startListening(intent: Intent, recognitionListener: ContinuousSpeechRecognitionListener) {
        speechRecognizer.destroy()
        speechRecognizer.setRecognitionListener(recognitionListener)

        intent.action = RecognizerIntent.ACTION_RECOGNIZE_SPEECH
        intent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)

        speechRecognizer.startListening(intent)
    }
}
