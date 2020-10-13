package com.stevenpopovich.talktothat.usbinterfacing

import android.widget.TextView
import com.stevenpopovich.talktothat.MainFragment
import com.stevenpopovich.talktothat.testutils.relaxedMock
import io.mockk.spyk
import io.mockk.verify
import io.reactivex.rxjava3.subjects.BehaviorSubject
import org.junit.Test

class SerialPortReaderTest {
    private val debugTextView: TextView = relaxedMock()
    private val mainFragment: MainFragment = relaxedMock()
    private val behaviorSubject: BehaviorSubject<ByteArray> = spyk(BehaviorSubject.create())

    @Test
    fun `test on read data`() {
        val serialPortReader = SerialPortReader(
            debugTextView,
            mainFragment,
            behaviorSubject = behaviorSubject
        )

        val byteArray = ByteArray(0)

        serialPortReader.onReceivedData(byteArray)

        verify { behaviorSubject.onNext(byteArray) }
    }

    @Test
    fun `optional parameter`() {
        val serialPortReader = SerialPortReader(
            debugTextView,
            mainFragment
        )
    }
}
