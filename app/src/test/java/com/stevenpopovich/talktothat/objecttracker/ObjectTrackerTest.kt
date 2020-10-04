package com.stevenpopovich.talktothat.objecttracker

import com.otaliastudios.cameraview.CameraView
import com.otaliastudios.cameraview.frame.Frame
import com.stevenpopovich.talktothat.testutils.relaxedMock
import org.junit.Test

class ObjectTrackerTest {
    private val cameraView: CameraView = relaxedMock()
    private val frame: Frame = relaxedMock()

    private val objectTracker: ObjectTracker = ObjectTracker(cameraView)

    @Test
    fun `object tracker can process frame`() {
        objectTracker.process(frame)
    }
}
