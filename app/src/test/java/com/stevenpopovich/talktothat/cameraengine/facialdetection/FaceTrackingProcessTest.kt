package com.stevenpopovich.talktothat.cameraengine.facialdetection

import org.junit.Assert
import org.junit.Test

class FaceTrackingProcessTest {
    @Test
    fun `test that we have a face tracking process object I guess lol`() {
        val process = FaceTrackingProcess(1.0, 2.0, 3.0)
        Assert.assertEquals(1.0, process.input, 0.0)
        Assert.assertEquals(2.0, process.output, 0.0)
        Assert.assertEquals(3.0, process.setpoint, 0.0)
    }
}
