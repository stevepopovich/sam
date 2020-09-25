package com.stevenpopovich.talktothat

import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import com.stevenpopovich.talktothat.testutils.relaxedMock
import com.stevenpopovich.talktothat.testutils.verifyExactlyOne
import org.junit.Assert.assertTrue
import org.junit.Test

class ContinuousSpeechRecognizerTest {
    private val speechRecognizer: SpeechRecognizer = relaxedMock()
    private val recognitionListener: ContinuousSpeechRecognitionListener = relaxedMock()
    private val intent: Intent = relaxedMock()

    private val continuousSpeechRecognizer = ContinuousSpeechRecognizer()

    @Test
    fun `can start listening`() {
        continuousSpeechRecognizer.startListening(speechRecognizer, recognitionListener, intent)

        verifyExactlyOne { speechRecognizer.destroy() }
        verifyExactlyOne { speechRecognizer.setRecognitionListener(recognitionListener) }
        verifyExactlyOne { intent.action = RecognizerIntent.ACTION_RECOGNIZE_SPEECH }
        verifyExactlyOne { intent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true) }
        verifyExactlyOne { speechRecognizer.startListening(intent) }
    }

    @Test
    fun `test type aliases`() {
        val businessLogic: SpeechResultBusinessLogic = relaxedMock()
        val restartLogic: SpeechRecognizerRestartLogic = relaxedMock()

        assertTrue(businessLogic is (results: Bundle?) -> Unit)
        assertTrue(restartLogic is (speechRecognitionListener: ContinuousSpeechRecognitionListener) -> Unit)
    }
}
