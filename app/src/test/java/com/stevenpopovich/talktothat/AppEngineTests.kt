package com.stevenpopovich.talktothat

import android.content.Intent
import com.stevenpopovich.talktothat.cameraengine.CameraEngine
import com.stevenpopovich.talktothat.speechrecognition.ContinuousSpeechRecognitionListener
import com.stevenpopovich.talktothat.speechrecognition.ContinuousSpeechRecognizer
import com.stevenpopovich.talktothat.testutils.relaxedMock
import com.stevenpopovich.talktothat.testutils.verifyExactlyOne
import io.mockk.confirmVerified
import org.junit.Test

class AppEngineTests {
    @Test
    fun start() {
        val continuousSpeechRecognizer: ContinuousSpeechRecognizer = relaxedMock()
        val cameraEngine: CameraEngine = relaxedMock()
        val intent: Intent = relaxedMock()
        val continuousSpeechRecognitionListener: ContinuousSpeechRecognitionListener = relaxedMock()

        val appEngine = AppEngine()

        appEngine.start(
            continuousSpeechRecognizer,
            cameraEngine,
            intent,
            continuousSpeechRecognitionListener
        )

        verifyExactlyOne { continuousSpeechRecognizer.startListening(intent, continuousSpeechRecognitionListener) }
        verifyExactlyOne { cameraEngine.start() }

        confirmVerified(continuousSpeechRecognizer)
    }

    @Test
    fun `optional parameters`() {
        MainDependencyModule.speechResultsBusinessLogic = relaxedMock()

        val continuousSpeechRecognizer: ContinuousSpeechRecognizer = relaxedMock()
        val cameraEngine: CameraEngine = relaxedMock()

        val appEngine = AppEngine()

        appEngine.start(
            continuousSpeechRecognizer,
            cameraEngine
        )

        verifyExactlyOne { continuousSpeechRecognizer.startListening(any(), any()) }
        verifyExactlyOne { cameraEngine.start() }

        confirmVerified(continuousSpeechRecognizer)
    }
}
