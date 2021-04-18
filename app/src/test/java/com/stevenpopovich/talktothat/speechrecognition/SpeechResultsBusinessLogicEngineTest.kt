package com.stevenpopovich.talktothat.speechrecognition

import android.os.Bundle
import android.speech.SpeechRecognizer
import android.widget.TextView
import com.stevenpopovich.talktothat.MainDependencyModule
import com.stevenpopovich.talktothat.taskmanager.TaskManager
import com.stevenpopovich.talktothat.testutils.relaxedMock
import com.stevenpopovich.talktothat.usbinterfacing.SerialPortInterface
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.verify
import org.junit.Test

class SpeechResultsBusinessLogicEngineTest {
    private val mainText: TextView = relaxedMock()
    private val serialPortInterface: SerialPortInterface = relaxedMock()
    private val taskManager: TaskManager = relaxedMock()

    private val speechResultsBusinessLogicEngine = SpeechResultsBusinessLogicEngine(
        mainText,
        taskManager,
        serialPortInterface
    )

    @Test
    fun `test passing forward, backward, left, right, stop passes those strings to the arduino`() {
        val bundle: Bundle = relaxedMock()
        val stringArrayList: ArrayList<String> = arrayListOf("forward, backward, left, right, stop")

        every { bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION) } returns stringArrayList

        speechResultsBusinessLogicEngine.onSpeechResults(bundle)

        verify { mainText.text = stringArrayList.toString() }

        confirmVerified(mainText, serialPortInterface)
    }

    @Test
    fun `test optional parameters`() {
        val bundle: Bundle = relaxedMock()

        MainDependencyModule.mainText = relaxedMock()
        MainDependencyModule.serialPortInterface = relaxedMock()
        val engine = SpeechResultsBusinessLogicEngine()

        engine.onSpeechResults(bundle)
    }
}
