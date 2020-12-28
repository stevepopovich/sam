package com.stevenpopovich.talktothat.cameraengine.facialdetection

import android.view.ViewGroupOverlay
import com.google.mlkit.vision.face.Face
import com.otaliastudios.cameraview.CameraView
import com.stevenpopovich.talktothat.testutils.relaxedMock
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.verifySequence
import org.junit.Test

class FaceDetectionSuccessListenerTest {
    @Test
    fun `test we draw detected faces`() {
        val cameraView: CameraView = relaxedMock()
        val face1: Face = relaxedMock()
        val face2: Face = relaxedMock()

        val overlay: ViewGroupOverlay = relaxedMock()
        val faces: MutableList<Face> = mutableListOf(face1, face2)

        every { face1.boundingBox } returns relaxedMock()
        every { face2.boundingBox } returns relaxedMock()

        every { cameraView.overlay } returns overlay

        every { overlay.clear() } returns Unit

        val listener = FaceDetectionSuccessListener(cameraView)

        listener.onSuccess(faces)

        verifySequence {
            cameraView.overlay
            cameraView.overlay
            face1.boundingBox
            cameraView.overlay
            face2.boundingBox
        }

        confirmVerified(cameraView, face1, face2)
    }
}
