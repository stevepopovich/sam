package com.stevenpopovich.talktothat.cameraengine.facialdetection

import com.stevenpopovich.talktothat.testutils.relaxedMock
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.verifySequence
import org.junit.Test

class PIDTest {
    @Test
    fun `test that compute does PID things in auto mode`() {
        val process: Process = relaxedMock()

        val pid = PID(
            process,
            1.0,
            2.0,
            2.5,
            ControllerDirection.DIRECT
        )

        every { process.setpoint } returns 6.0
        every { process.input } returns 3.0

        pid.compute()
        Thread.sleep(150)
        pid.compute()
        Thread.sleep(150)
        pid.compute()

        verifySequence {
            process.output
            process.input
            process.setpoint
            process.input
            process.input
            process.output = 0.0
            process.input
            process.setpoint
            process.input
            process.input
            process.output = 4.2
            process.input
            process.setpoint
            process.input
            process.input
            process.output = 4.800000000000001
            process.input
        }

        confirmVerified(process)
    }

    @Test
    fun `test that putting the PID in reverse gives reversed outputs`() {
        val process: Process = relaxedMock()

        val pid = PID(
            process,
            1.0,
            2.0,
            2.5,
            ControllerDirection.REVERSE
        )

        every { process.setpoint } returns 6.0
        every { process.input } returns 3.0

        pid.setOutputLimits(-255.0, 0.0)
        pid.compute()
        Thread.sleep(150)
        pid.compute()
        Thread.sleep(150)
        pid.compute()

        verifySequence {
            process.output
            process.input
            process.setpoint
            process.input
            process.input
            process.output = 0.0
            process.input
            process.setpoint
            process.input
            process.input
            process.output = -4.2
            process.input
            process.setpoint
            process.input
            process.input
            process.output = -4.800000000000001
            process.input
        }

        confirmVerified(process)
    }

    @Test
    fun `test that we have optional parameters`() {
        val process: Process = relaxedMock()

        PID(
            process,
            3.4,
            3.5,
            2.5
        )
    }
}
