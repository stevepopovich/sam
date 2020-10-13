package com.stevenpopovich.talktothat.usbinterfacing

import android.widget.TextView
import com.stevenpopovich.talktothat.MainFragment

class OnReadFromSerialPortLogic(
    private val debugTextView: TextView,
    private val mainFragment: MainFragment,
) {
    private var currentBytesToPrint: ByteArray = ByteArray(0)

    val logic = {
        val stringToOutput = currentBytesToPrint.toString(Charsets.UTF_8)
        if (stringToOutput.isNotBlank()) {
            mainFragment.activity?.runOnUiThread {
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
