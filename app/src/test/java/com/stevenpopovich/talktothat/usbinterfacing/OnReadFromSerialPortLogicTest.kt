package com.stevenpopovich.talktothat.usbinterfacing

import android.widget.TextView
import com.stevenpopovich.talktothat.testutils.relaxedMock
import io.mockk.every
import io.mockk.slot
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Test

class OnReadFromSerialPortLogicTest {
    private val debugTextView: TextView = relaxedMock()
    private val runOnUIThread: (action: Runnable) -> Unit = relaxedMock()

    private val onReadFromSerialPortLogic = OnReadFromSerialPortLogic(debugTextView, runOnUIThread)

    @Test
    fun `test buffer bytes`() {
        val bytesToAdd = "thing".toByteArray()

        val gottenBytes = onReadFromSerialPortLogic.bufferBytesToPrint(bytesToAdd)

        assertEquals(bytesToAdd.contentToString(), gottenBytes.contentToString())
    }

    @Test
    fun `test core logic`() {
        val runOnUIThreadSlot = slot<Runnable>()
        every { runOnUIThread(capture(runOnUIThreadSlot)) } returns Unit

        onReadFromSerialPortLogic.bufferBytesToPrint("random".toByteArray())

        onReadFromSerialPortLogic.logic()

        verify { runOnUIThread(any()) }

        runOnUIThreadSlot.captured.run()
        verify { debugTextView.text = "random" }
    }
}
