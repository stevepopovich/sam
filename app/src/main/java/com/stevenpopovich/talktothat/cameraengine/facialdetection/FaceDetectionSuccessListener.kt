package com.stevenpopovich.talktothat.cameraengine.facialdetection

import com.google.android.gms.tasks.OnSuccessListener
import com.google.mlkit.vision.face.Face
import com.otaliastudios.cameraview.CameraView
import com.stevenpopovich.talktothat.cameraengine.RectangleDrawable

class FaceDetectionSuccessListener(
    private val cameraView: CameraView
) : OnSuccessListener<MutableList<Face>> {
    override fun onSuccess(faces: MutableList<Face>?) {
        cameraView.overlay.clear()
        faces?.forEach {
            cameraView.overlay.add(RectangleDrawable(it.boundingBox))
        }
    }
}
