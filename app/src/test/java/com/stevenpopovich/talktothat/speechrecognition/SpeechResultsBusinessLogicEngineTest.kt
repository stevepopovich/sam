package com.stevenpopovich.talktothat.speechrecognition

import android.os.Bundle
import android.speech.SpeechRecognizer
import android.widget.TextView
import com.stevenpopovich.talktothat.MainDependencyModule
import com.stevenpopovich.talktothat.taskmanager.TaskManager
import com.stevenpopovich.talktothat.taskmanager.tasks.ComeHereTask
import com.stevenpopovich.talktothat.taskmanager.tasks.DoASpinTask
import com.stevenpopovich.talktothat.taskmanager.tasks.DontTouchTask
import com.stevenpopovich.talktothat.testutils.relaxedMock
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.verify
import org.junit.Test

class SpeechResultsBusinessLogicEngineTest {
    private val mainText: TextView = relaxedMock()
    private val taskManager: TaskManager = relaxedMock()
    private val comeHereTask: ComeHereTask = relaxedMock()
    private val doASpinTask: DoASpinTask = relaxedMock()
    private val dontTouchTask: DontTouchTask = relaxedMock()

    private val speechResultsBusinessLogicEngine = SpeechResultsBusinessLogicEngine(
        mainText,
        taskManager,
        comeHereTask,
        doASpinTask,
        dontTouchTask
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
        val stringArray = arrayListOf("hey Sam come here")

        every { bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION) } returns stringArray

        speechResultsBusinessLogicEngine.onSpeechResults(bundle)

        verify { mainText.text = stringArray.toString() }
        verify { taskManager.runTask(comeHereTask) }

        confirmVerified(mainText, taskManager, comeHereTask)
    }

    @Test
    fun `test saying do a spin creates a do a spin task`() {
        val bundle: Bundle = relaxedMock()
        val stringArray = arrayListOf("do a spin")

        every { bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION) } returns stringArray

        speechResultsBusinessLogicEngine.onSpeechResults(bundle)

        verify { mainText.text = stringArray.toString() }
        verify { taskManager.runTask(doASpinTask) }

        confirmVerified(mainText, taskManager, doASpinTask)
    }

    @Test
    fun `test saying dont touch creates a dont touch task`() {
        val bundle: Bundle = relaxedMock()
        val stringArray = arrayListOf("don't touch")

        every { bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION) } returns stringArray

        speechResultsBusinessLogicEngine.onSpeechResults(bundle)

        verify { mainText.text = stringArray.toString() }
        verify { taskManager.runTask(dontTouchTask) }

        confirmVerified(mainText, taskManager, dontTouchTask)
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
