package com.stevenpopovich.talktothat.speechrecognition

import android.os.Bundle
import android.speech.SpeechRecognizer
import android.widget.TextView
import com.stevenpopovich.talktothat.MainDependencyModule
import com.stevenpopovich.talktothat.taskmanager.ComeHereTask
import com.stevenpopovich.talktothat.taskmanager.TaskManager
import com.stevenpopovich.talktothat.testutils.relaxedMock
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.verify
import org.junit.Test

class SpeechResultsBusinessLogicEngineTest {
    private val mainText: TextView = relaxedMock()
    private val taskManager: TaskManager = relaxedMock()
    private val comeHereTask: ComeHereTask = relaxedMock()

    private val speechResultsBusinessLogicEngine = SpeechResultsBusinessLogicEngine(
        mainText,
        taskManager,
        comeHereTask
    )

    @Test
    fun `test saying stop calls stop on the task manager`() {
        val bundle: Bundle = relaxedMock()
        val stopString = arrayListOf("stop")

        every { bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION) } returns stopString

        speechResultsBusinessLogicEngine.onSpeechResults(bundle)

        verify { mainText.text = stopString.toString() }
        verify { taskManager.stop() }

        confirmVerified(mainText, taskManager)
    }

    @Test
    fun `test saying hey sam come here creates a come here task`() {
        val bundle: Bundle = relaxedMock()
        val comeHereString = arrayListOf("hey Sam come here")

        every { bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION) } returns comeHereString

        speechResultsBusinessLogicEngine.onSpeechResults(bundle)

        verify { mainText.text = comeHereString.toString() }
        verify { taskManager.runTask(comeHereTask) }

        confirmVerified(mainText, taskManager)
    }

    @Test
    fun `test optional parameters`() {
        val bundle: Bundle = relaxedMock()

        MainDependencyModule.mainText = relaxedMock()
        MainDependencyModule.serialPortInterface = relaxedMock()
        MainDependencyModule.arduinoInterface = relaxedMock()
        val engine = SpeechResultsBusinessLogicEngine()

        engine.onSpeechResults(bundle)
    }
}
