package com.stevenpopovich.talktothat

import android.annotation.SuppressLint
import android.hardware.camera2.CameraCaptureSession
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraDevice
import android.hardware.camera2.CameraManager
import android.hardware.camera2.CaptureRequest
import android.hardware.camera2.CaptureResult
import android.hardware.camera2.TotalCaptureResult
import android.hardware.camera2.params.OutputConfiguration
import android.hardware.camera2.params.SessionConfiguration
import android.view.Surface

class ContinuousFaceDetector {
    @SuppressLint("MissingPermission")
    fun start(cameraManager: CameraManager, surface: Surface) {

        val frontCameraId = cameraManager.cameraIdList.first {
            cameraManager.getCameraCharacteristics(it)
                .get(CameraCharacteristics.LENS_FACING) == CameraCharacteristics.LENS_FACING_FRONT
        }

        cameraManager.openCamera(
            frontCameraId,
            CameraCallBack(surface),
            null
        )
    }
}

class CameraCallBack(
    private val surface: Surface
) : CameraDevice.StateCallback() {
    override fun onOpened(camera: CameraDevice) {
        "camera opened".verboseLog()
        camera
            .createCaptureSession(
                SessionConfiguration(
                    SessionConfiguration.SESSION_REGULAR,
                    listOf(OutputConfiguration(surface)),
                    { it.run() },
                    CaptureCallback(surface)
                )
            )
    }

    override fun onDisconnected(camera: CameraDevice) {}

    override fun onError(camera: CameraDevice, error: Int) {}
}

class CaptureCallback(
    private val surface: Surface
) : CameraCaptureSession.StateCallback() {
    override fun onConfigured(session: CameraCaptureSession) {
        "on configured".verboseLog()
        val captureRequest = session.device
            .createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)

        captureRequest.set(CaptureRequest.STATISTICS_FACE_DETECT_MODE, CameraCharacteristics.STATISTICS_FACE_DETECT_MODE_FULL)
        captureRequest.addTarget(surface)

        session.setRepeatingRequest(captureRequest.build(), CaptureCallbackListener(), null)
    }

    override fun onConfigureFailed(session: CameraCaptureSession) {}
}

class CaptureCallbackListener : CameraCaptureSession.CaptureCallback() {
    override fun onCaptureCompleted(
        session: CameraCaptureSession,
        request: CaptureRequest,
        result: TotalCaptureResult
    ) {
        super.onCaptureCompleted(session, request, result)

        _ = result.get(CaptureResult.STATISTICS_FACES) // faces
    }
}
