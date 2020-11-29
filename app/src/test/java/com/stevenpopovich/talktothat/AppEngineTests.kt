package com.stevenpopovich.talktothat

import android.content.Intent
import com.stevenpopovich.talktothat.speechrecognition.ContinuousSpeechRecognitionListener
import com.stevenpopovich.talktothat.speechrecognition.ContinuousSpeechRecognizer
import com.stevenpopovich.talktothat.speechrecognition.SpeechResultsBusinessLogicEngine
import com.stevenpopovich.talktothat.testutils.relaxedMock
import com.stevenpopovich.talktothat.testutils.verifyExactlyOne
import io.mockk.confirmVerified
import org.junit.Test

class AppEngineTests {
    @Test
    fun start() {
        val continuousSpeechRecognizer: ContinuousSpeechRecognizer = relaxedMock()
        val logicEngine: SpeechResultsBusinessLogicEngine = relaxedMock()
        val intent: Intent = relaxedMock()
        val continuousSpeechRecognitionListener: ContinuousSpeechRecognitionListener = relaxedMock()

        val appEngine = AppEngine()

        appEngine.start(
            continuousSpeechRecognizer,
            logicEngine,
            intent,
            continuousSpeechRecognitionListener
        )

        verifyExactlyOne { continuousSpeechRecognizer.startListening(intent, continuousSpeechRecognitionListener) }

        confirmVerified(continuousSpeechRecognizer, logicEngine)
    }

    @Test
    fun `optional parameters`() {
        val continuousSpeechRecognizer: ContinuousSpeechRecognizer = relaxedMock()
        val logicEngine: SpeechResultsBusinessLogicEngine = relaxedMock()

        val appEngine = AppEngine()

        appEngine.start(
            continuousSpeechRecognizer,
            logicEngine,
        )

        verifyExactlyOne { continuousSpeechRecognizer.startListening(any(), any()) }

        confirmVerified(continuousSpeechRecognizer, logicEngine)
    }
}
