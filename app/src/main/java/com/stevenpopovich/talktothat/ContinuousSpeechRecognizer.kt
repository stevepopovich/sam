package com.stevenpopovich.talktothat

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer

typealias SpeechResultBusinessLogic = (results: Bundle?) -> Unit

class ContinuousSpeechRecognizer private constructor() {
    private var speechRecognizer: SpeechRecognizer? = null

    companion object {
        val instance: ContinuousSpeechRecognizer by lazy {
            ContinuousSpeechRecognizer()
        }
    }

    fun startListening(businessLogic: SpeechResultBusinessLogic, applicationContext: Context) {
        speechRecognizer?.destroy()

        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(applicationContext)

        speechRecognizer?.let {
            it.setRecognitionListener(continuousSpeechRecognitionListener(businessLogic, applicationContext))

            val speechRecognizerIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)

            speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)

            it.startListening(speechRecognizerIntent)
        }
    }
}

fun continuousSpeechRecognitionListener(businessLogic: SpeechResultBusinessLogic, applicationContext: Context) = object : RecognitionListener {
    override fun onReadyForSpeech(params: Bundle?) {}
    override fun onBeginningOfSpeech() {}
    override fun onRmsChanged(rmsdB: Float) {}
    override fun onBufferReceived(buffer: ByteArray?) {}
    override fun onEndOfSpeech() {}
    override fun onError(error: Int) {
        if (error.toRecognizerError == "ERROR_NO_MATCH") {
            ContinuousSpeechRecognizer.instance.startListening(businessLogic, applicationContext)
        }
    }
    
    override fun onResults(results: Bundle?) {
        businessLogic(results)

        ContinuousSpeechRecognizer.instance.startListening(businessLogic, applicationContext)
    }

    override fun onPartialResults(partialResults: Bundle?) {
        businessLogic(partialResults)
    }

    override fun onEvent(eventType: Int, params: Bundle?) {}
}