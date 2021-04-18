package com.stevenpopovich.talktothat.usbinterfacing

import com.stevenpopovich.talktothat.MainDependencyModule
import com.stevenpopovich.talktothat.testutils.relaxedMock
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import org.junit.Test

class SerialPortReaderTest {
    private val behaviorSubject: BehaviorSubject<ByteArray> = spyk(BehaviorSubject.create())
    private val compositeDisposable: CompositeDisposable = mockk(relaxed = true)
    private val onReadFromSerialPortLogic: OnReadFromSerialPortLogic = mockk(relaxed = true)

    @Test
    fun `test on read data`() {
        val serialPortReader = SerialPortReader(
            behaviorSubject = behaviorSubject,
            onReadFromSerialPortLogic,
            compositeDisposable
        )

        val byteArray = ByteArray(0)

        serialPortReader.onReceivedData(byteArray)

        verify { behaviorSubject.onNext(byteArray) }
    }

    @Test
    fun `optional parameter`() {
        MainDependencyModule.onReadFromSerialPortLogic = relaxedMock()
        SerialPortReader()
    }
}
