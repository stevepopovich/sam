package com.stevenpopovich.talktothat.cameraengine

import com.google.mlkit.vision.common.InputImage
import com.otaliastudios.cameraview.frame.Frame

fun buildImageFromFrame(frame: Frame): InputImage {
    val imageAsByteArray: ByteArray = frame.getData()
    return InputImage.fromByteArray(
        imageAsByteArray,
        frame.size.width,
        frame.size.height,
        frame.rotationToView,
        frame.format
    )
}
