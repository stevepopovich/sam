package com.stevenpopovich.talktothat

import android.os.Bundle
import android.speech.RecognitionListener

class ContinuousSpeechRecognitionListener(
    private val businessLogic: SpeechResultBusinessLogic,
    private val speechRecognizerRestartLogic: SpeechRecognizerRestartLogic,
) : RecognitionListener {
    override fun onReadyForSpeech(params: Bundle?) {}
    override fun onBeginningOfSpeech() {}
    override fun onRmsChanged(rmsdB: Float) {}
    override fun onBufferReceived(buffer: ByteArray?) {}
    override fun onEndOfSpeech() {}
    override fun onError(error: Int) {
        if (error.toRecognizerError == "ERROR_NO_MATCH") {
            speechRecognizerRestartLogic(this)
        }
    }

    override fun onResults(results: Bundle?) {
        businessLogic(results)

        speechRecognizerRestartLogic(this)
    }

    override fun onPartialResults(partialResults: Bundle?) {
        businessLogic(partialResults)
    }

    override fun onEvent(eventType: Int, params: Bundle?) {}
}
