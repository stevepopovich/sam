package com.stevenpopovich.talktothat.speechrecognition

import android.os.Bundle
import android.speech.SpeechRecognizer
import android.widget.TextView
import com.stevenpopovich.talktothat.MainDependencyModule
import com.stevenpopovich.talktothat.taskmanager.TaskManager
import com.stevenpopovich.talktothat.usbinterfacing.SerialPortInterface

class SpeechResultsBusinessLogicEngine(
    private val mainText: TextView = MainDependencyModule.mainText,
    private val taskManager: TaskManager = MainDependencyModule.taskManager,
    private val serialPortInterface: SerialPortInterface = MainDependencyModule.serialPortInterface
) {
    fun onSpeechResults(results: Bundle?) {
        serialPortInterface?.let { serialPort ->
            val speechResults = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)

            mainText.text = speechResults.toString()

            if (speechResults?.any { it.contains("hey sam") } == true) {
//                taskManager.runTask(LookAtMeTask())
            }
        }
    }
}
