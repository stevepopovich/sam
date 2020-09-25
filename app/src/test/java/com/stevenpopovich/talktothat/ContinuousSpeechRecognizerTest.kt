package com.stevenpopovich.talktothat

import android.content.Intent
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import com.stevenpopovich.talktothat.testutils.relaxedMock
import com.stevenpopovich.talktothat.testutils.verifyExactlyOne
import org.junit.Test

class ContinuousSpeechRecognizerTest {
    private val speechRecognizer: SpeechRecognizer = relaxedMock()
    private val recognitionListener: ContinuousSpeechRecognitionListener = relaxedMock()
    private val intent: Intent = relaxedMock()

    private val continuousSpeechRecognizer = ContinuousSpeechRecognizer(speechRecognizer)

    @Test
    fun `can start listening`() {
        continuousSpeechRecognizer.startListening(intent, recognitionListener)

        verifyExactlyOne { speechRecognizer.destroy() }
        verifyExactlyOne { speechRecognizer.setRecognitionListener(recognitionListener) }
        verifyExactlyOne { intent.action = RecognizerIntent.ACTION_RECOGNIZE_SPEECH }
        verifyExactlyOne { intent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true) }
        verifyExactlyOne { speechRecognizer.startListening(intent) }
    }
}
