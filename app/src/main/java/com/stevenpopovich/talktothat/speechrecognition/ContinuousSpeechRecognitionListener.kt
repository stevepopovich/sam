package com.stevenpopovich.talktothat.speechrecognition

import android.os.Bundle
import android.speech.RecognitionListener
import com.stevenpopovich.talktothat.MainDependencyModule

class ContinuousSpeechRecognitionListener(
    private val speechResultsBusinessLogic: SpeechResultBusinessLogic = MainDependencyModule.speechResultsBusinessLogic,
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
        speechResultsBusinessLogic(results)

        speechRecognizerRestartLogic(this)
    }

    override fun onPartialResults(partialResults: Bundle?) {
        speechResultsBusinessLogic(partialResults)
    }

    override fun onEvent(eventType: Int, params: Bundle?) {}
}
