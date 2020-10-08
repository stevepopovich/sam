package com.stevenpopovich.talktothat.objecttracker

import com.google.android.gms.tasks.OnSuccessListener
import com.google.mlkit.vision.objects.DetectedObject
import com.otaliastudios.cameraview.CameraView

class DetectedObjectSuccessListener(
    private val cameraView: CameraView,
) : OnSuccessListener<MutableList<DetectedObject>> {
    override fun onSuccess(objects: MutableList<DetectedObject>?) {
        cameraView.overlay.clear()
        objects?.forEach {
            if (it.labels.map { it.text }.contains("Fashion good")) {
                cameraView.overlay.add(RectangleDrawable(it.boundingBox))
            }
        }
    }
}
