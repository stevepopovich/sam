package com.stevenpopovich.talktothat

import com.stevenpopovich.talktothat.testutils.relaxedMock
import com.stevenpopovich.talktothat.testutils.verifyExactlyOne
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
    }
}
