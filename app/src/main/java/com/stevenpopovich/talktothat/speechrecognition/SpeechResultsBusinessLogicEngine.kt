package com.stevenpopovich.talktothat.speechrecognition

import android.os.Bundle
import android.speech.SpeechRecognizer
import android.widget.TextView
import com.stevenpopovich.talktothat.MainDependencyModule
import com.stevenpopovich.talktothat.taskmanager.TaskManager
import com.stevenpopovich.talktothat.taskmanager.tasks.ComeHereTask
import com.stevenpopovich.talktothat.taskmanager.tasks.DoASpinTask

class SpeechResultsBusinessLogicEngine(
    private val mainText: TextView = MainDependencyModule.mainText,
    private val taskManager: TaskManager = MainDependencyModule.taskManager,
    private val comeHereTask: ComeHereTask = ComeHereTask(),
    private val doASpinTask: DoASpinTask = DoASpinTask(),
) {
    fun onSpeechResults(results: Bundle?) {
        val speechResults = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
        mainText.text = speechResults.toString()

        if (speechResults?.any { it.contains("stop") } == true) {
            taskManager.stop()
        }

        if (speechResults?.any { it.contains("come here") } == true) {
            taskManager.runTask(comeHereTask)
        }

        if (speechResults?.any { it.contains("do a spin") } == true) {
            taskManager.runTask(doASpinTask)
        }
    }
}
