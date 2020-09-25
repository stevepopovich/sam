package com.stevenpopovich.talktothat

import android.os.Bundle
import io.mockk.Called
import io.mockk.verify
import org.junit.Test

class ContinuousSpeechRecognitionListenerTests {
    private val businessLogic: SpeechResultBusinessLogic = relaxedMock()
    private val restartLogic: SpeechRecognizerRestartLogic = relaxedMock()

    private val continuousSpeechRecognitionListener = ContinuousSpeechRecognitionListener(businessLogic, restartLogic)

    @Test
    fun `recognition listener restarts listener when no match error`() {
        continuousSpeechRecognitionListener.onError(7)

        verifyExactlyOne { restartLogic(continuousSpeechRecognitionListener) }
    }

    @Test
    fun `recognition listener onResults calls business logic and restarts listener`() {
        val bundle: Bundle? = relaxedMock()

        continuousSpeechRecognitionListener.onResults(bundle)

        verifyExactlyOne { businessLogic(bundle) }
        verifyExactlyOne { restartLogic(continuousSpeechRecognitionListener) }
    }

    @Test
    fun `recognition listener onPartialResults calls business logic`() {
        val bundle: Bundle? = relaxedMock()

        continuousSpeechRecognitionListener.onPartialResults(bundle)

        verifyExactlyOne { businessLogic(bundle) }
    }

    @Test
    fun `verify no logic was called in any other hook`() {
        continuousSpeechRecognitionListener.onReadyForSpeech(null)
        continuousSpeechRecognitionListener.onBeginningOfSpeech()
        continuousSpeechRecognitionListener.onRmsChanged(-1f)
        continuousSpeechRecognitionListener.onBufferReceived(null)
        continuousSpeechRecognitionListener.onEndOfSpeech()
        continuousSpeechRecognitionListener.onEvent(-1, null)

        verify { businessLogic(any()) wasNot Called }
        verify { restartLogic(any()) wasNot Called }
    }
}
