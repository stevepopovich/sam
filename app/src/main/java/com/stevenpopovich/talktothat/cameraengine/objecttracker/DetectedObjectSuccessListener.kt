package com.stevenpopovich.talktothat.cameraengine.objecttracker

import com.google.android.gms.tasks.OnSuccessListener
import com.google.mlkit.vision.objects.DetectedObject
import com.otaliastudios.cameraview.CameraView
import com.stevenpopovich.talktothat.cameraengine.RectangleDrawable

class DetectedObjectSuccessListener(
    private val cameraView: CameraView,
) : OnSuccessListener<MutableList<DetectedObject>> {
    override fun onSuccess(objects: MutableList<DetectedObject>?) {
        cameraView.overlay.clear()
        objects?.forEach {
            cameraView.overlay.add(RectangleDrawable(it.boundingBox))
        }
    }
}
