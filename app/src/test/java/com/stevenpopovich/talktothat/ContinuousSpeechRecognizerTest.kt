package com.stevenpopovich.talktothat

import android.content.Intent
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import org.junit.Test

class ContinuousSpeechRecognizerTest {
    private val speechRecognizer: SpeechRecognizer = relaxedMock()
    private val mockRecognitionListener: ContinuousSpeechRecognitionListener = relaxedMock()
    private val intent: Intent = relaxedMock()

    private val continuousSpeechRecognizer = ContinuousSpeechRecognizer()

    @Test
    fun `can start listening`() {
        continuousSpeechRecognizer.startListening(speechRecognizer, mockRecognitionListener, intent)

        verifyExactlyOne { speechRecognizer.destroy() }
        verifyExactlyOne { speechRecognizer.setRecognitionListener(mockRecognitionListener) }
        verifyExactlyOne { intent.action = RecognizerIntent.ACTION_RECOGNIZE_SPEECH }
        verifyExactlyOne { intent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true) }
        verifyExactlyOne { speechRecognizer.startListening(intent) }
    }
}
