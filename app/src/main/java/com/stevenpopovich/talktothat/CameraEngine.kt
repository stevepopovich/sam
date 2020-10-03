package com.stevenpopovich.talktothat

import android.annotation.SuppressLint
import android.graphics.ImageFormat
import android.hardware.camera2.CameraCaptureSession
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraDevice
import android.hardware.camera2.CameraManager
import android.hardware.camera2.params.OutputConfiguration
import android.hardware.camera2.params.SessionConfiguration
import android.media.ImageReader
import android.os.Handler
import android.os.Looper
import android.view.Surface
import android.view.SurfaceView
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class CameraEngine {
    @SuppressLint("MissingPermission")
    fun start(cameraManager: CameraManager, surface: SurfaceView, objectTracker: ObjectTracker) {

        val frontCameraId = cameraManager.cameraIdList.first {
            cameraManager.getCameraCharacteristics(it)
                .get(CameraCharacteristics.LENS_FACING) == CameraCharacteristics.LENS_FACING_FRONT
        }

        cameraManager.openCamera(
            frontCameraId,
            CameraCallBack(surface.holder.surface, objectTracker),
            null
        )
    }
}

class CameraCallBack(
    private val surface: Surface,
    private val objectTracker: ObjectTracker
) : CameraDevice.StateCallback() {
    override fun onOpened(camera: CameraDevice) {
        "camera opened".verboseLog()

        GlobalScope.launch {
            repeat(20) {
                val imageAvailableListener = ImageAvailableListener(objectTracker)

                val imageReader = ImageReader.newInstance(100, 100, ImageFormat.YUV_420_888, 3)
                imageReader.setOnImageAvailableListener(imageAvailableListener, Handler(Looper.getMainLooper()))

                camera
                    .createCaptureSession(
                        SessionConfiguration(
                            SessionConfiguration.SESSION_REGULAR,
                            listOf(
                                OutputConfiguration(surface),
                                OutputConfiguration(imageReader.surface)
                            ),
                            { it.run() },
                            CaptureCallback(surface, imageReader)
                        )
                    )
            }
        }
    }

    override fun onDisconnected(camera: CameraDevice) {
        "disconnected camera".verboseLog()
    }

    override fun onError(camera: CameraDevice, error: Int) {
        "on error".verboseLog()
    }
}

class CaptureCallback(
    private val surface: Surface,
    private val imageReader: ImageReader
) : CameraCaptureSession.StateCallback() {
    override fun onConfigured(session: CameraCaptureSession) {
        val captureRequest = session.device
            .createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)

        captureRequest.addTarget(imageReader.surface)
        captureRequest.addTarget(surface)

        session.capture(captureRequest.build(), CaptureCallbackListener(), Handler(Looper.getMainLooper()))
    }

    override fun onConfigureFailed(session: CameraCaptureSession) {}
}

class CaptureCallbackListener : CameraCaptureSession.CaptureCallback()

class ImageAvailableListener(private val objectTracker: ObjectTracker) : ImageReader.OnImageAvailableListener {
    override fun onImageAvailable(reader: ImageReader?) {
        val image = reader?.acquireLatestImage()
        if (image != null) {
            objectTracker.processImage(image)
        }
        reader?.close()
    }
}
