package com.stevenpopovich.talktothat.usbinterfacing

import android.widget.TextView
import com.stevenpopovich.talktothat.MainFragment
import com.stevenpopovich.talktothat.testutils.relaxedMock
import io.mockk.every
import io.mockk.slot
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Test

class OnReadFromSerialPortLogicTest {
    private val debugTextView: TextView = relaxedMock()
    private val mainFragment: MainFragment = relaxedMock()

    private val onReadFromSerialPortLogic = OnReadFromSerialPortLogic(debugTextView, mainFragment)

    @Test
    fun `test buffer bytes`() {
        val bytesToAdd = "thing".toByteArray()

        val gottenBytes = onReadFromSerialPortLogic.bufferBytesToPrint(bytesToAdd)

        assertEquals(bytesToAdd.contentToString(), gottenBytes.contentToString())
    }

    @Test
    fun `test core logic`() {
        val runOnUIThreadSlot = slot<Runnable>()
        every { mainFragment.activity?.runOnUiThread(capture(runOnUIThreadSlot)) } returns Unit

        onReadFromSerialPortLogic.bufferBytesToPrint("random".toByteArray())

        onReadFromSerialPortLogic.logic()

        verify { mainFragment.activity?.runOnUiThread(any()) }

        runOnUIThreadSlot.captured.run()
        verify { debugTextView.text = "random" }
    }
}
