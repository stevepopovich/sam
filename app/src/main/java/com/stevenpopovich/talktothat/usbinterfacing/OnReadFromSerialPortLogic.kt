package com.stevenpopovich.talktothat.usbinterfacing

import android.widget.TextView
import com.stevenpopovich.talktothat.MainDependencyModule

class OnReadFromSerialPortLogic(
    private val debugTextView: TextView = MainDependencyModule.debugText,
    private val runOnUIThread: (action: Runnable) -> Unit = MainDependencyModule.runOnUIThread,
) {
    private var currentBytesToPrint: ByteArray = ByteArray(0)

    val logic = {
        val stringToOutput = currentBytesToPrint.toString(Charsets.UTF_8)
        if (stringToOutput.isNotBlank()) {
            runOnUIThread {
                debugTextView.text = stringToOutput
            }
        }
        currentBytesToPrint = ByteArray(0)
    }

    fun bufferBytesToPrint(bytes: ByteArray): ByteArray {
        currentBytesToPrint += bytes
        return currentBytesToPrint
    }
}
