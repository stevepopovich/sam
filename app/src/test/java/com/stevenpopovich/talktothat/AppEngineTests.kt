package com.stevenpopovich.talktothat

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

        val appEngine = AppEngine()

        appEngine.start(
            continuousSpeechRecognizer,
            logicEngine
        )

        verifyExactlyOne { continuousSpeechRecognizer.startListening(any(), any()) }

        confirmVerified(continuousSpeechRecognizer, logicEngine)
    }
}
