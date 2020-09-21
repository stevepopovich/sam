package com.stevenpopovich.talktothat

import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer

typealias SpeechResultBusinessLogic = (results: Bundle?) -> Unit

class ContinuousSpeechRecognizer private constructor() {
    companion object {
        val instance: ContinuousSpeechRecognizer by lazy {
            ContinuousSpeechRecognizer()
        }

        fun recognitionListener(businessLogic: SpeechResultBusinessLogic, speechRecognizer: SpeechRecognizer, intent: Intent) = object : RecognitionListener {
            override fun onReadyForSpeech(params: Bundle?) {}
            override fun onBeginningOfSpeech() {}
            override fun onRmsChanged(rmsdB: Float) {}
            override fun onBufferReceived(buffer: ByteArray?) {}
            override fun onEndOfSpeech() {}
            override fun onError(error: Int) {
                if (error.toRecognizerError == "ERROR_NO_MATCH") {
                    instance.startListening(speechRecognizer, this, intent)
                }
            }

            override fun onResults(results: Bundle?) {
                businessLogic(results)

                instance.startListening(speechRecognizer, this, intent)
            }

            override fun onPartialResults(partialResults: Bundle?) {
                businessLogic(partialResults)
            }

            override fun onEvent(eventType: Int, params: Bundle?) {}
        }
    }

    fun startListening(speechRecognizer: SpeechRecognizer, recognitionListener: RecognitionListener, intent: Intent) {
        speechRecognizer.destroy()
        speechRecognizer.setRecognitionListener(recognitionListener)

        intent.action = RecognizerIntent.ACTION_RECOGNIZE_SPEECH
        intent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)

        speechRecognizer.startListening(intent)
    }
}
