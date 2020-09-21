package com.stevenpopovich.talktothat

import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import org.junit.Test

class ContinuousSpeechRecognizerTest {
    private val speechRecognizer: SpeechRecognizer = relaxedMock()
    private val mockRecognitionListener: RecognitionListener = relaxedMock()
    private val intent: Intent = relaxedMock()
    private val speechResultBusinessLogic: SpeechResultBusinessLogic = relaxedMock()

    private val recognitionListener = ContinuousSpeechRecognizer.recognitionListener(
        speechResultBusinessLogic,
        speechRecognizer,
        intent
    )

    @Test
    fun `can start listening`() {
        ContinuousSpeechRecognizer.instance.startListening(speechRecognizer, mockRecognitionListener, intent)

        verifyExactlyOne { speechRecognizer.destroy() }
        verifyExactlyOne { speechRecognizer.setRecognitionListener(mockRecognitionListener) }
        verifyExactlyOne { intent.action = RecognizerIntent.ACTION_RECOGNIZE_SPEECH }
        verifyExactlyOne { intent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true) }
        verifyExactlyOne { speechRecognizer.startListening(intent) }
    }

    @Test
    fun `recognition listener restarts listener when no match error`() {
        recognitionListener.onError(7)

        verifyExactlyOne { ContinuousSpeechRecognizer.instance.startListening(speechRecognizer, recognitionListener, intent) }
    }

    @Test
    fun `recognition listener onResults calls business logic and restarts listener`() {
        val bundle: Bundle? = relaxedMock()

        recognitionListener.onResults(bundle)

        verifyExactlyOne { speechResultBusinessLogic(bundle) }
        verifyExactlyOne { ContinuousSpeechRecognizer.instance.startListening(speechRecognizer, recognitionListener, intent) }
    }

    @Test
    fun `recognition listener onPartialResults calls business logic`() {
        val bundle: Bundle? = relaxedMock()

        recognitionListener.onPartialResults(bundle)

        verifyExactlyOne { speechResultBusinessLogic(bundle) }
    }
}
