package com.stevenpopovich.talktothat.taskmanager

import com.otaliastudios.cameraview.CameraView
import com.stevenpopovich.talktothat.MainDependencyModule
import com.stevenpopovich.talktothat.cameraengine.facialdetection.FaceDetectionSuccessListener
import com.stevenpopovich.talktothat.cameraengine.facialdetection.FaceTrackingEngine
import com.stevenpopovich.talktothat.verboseLog
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import java.util.concurrent.TimeUnit
import kotlin.random.Random

class LookAtMeTask(
    private val camera: CameraView = MainDependencyModule.camera,
    private val faceTrackingEngine: FaceTrackingEngine = MainDependencyModule.faceTrackingEngine,
) : Task {
    private val taskTime = Random.nextInt(5, 15) to TimeUnit.SECONDS

    private val disposable = CompositeDisposable()

    override fun start() {
        FaceDetectionSuccessListener.stopOverride.onNext(false)
        camera.clearFrameProcessors()
        camera.addFrameProcessor(faceTrackingEngine)

        Single.timer(3L, taskTime.second)
            .subscribe { _ -> finish() }
            .addTo(disposable)
    }

    override fun finish() {
        "setting stop override to true".verboseLog()
        FaceDetectionSuccessListener.stopOverride.onNext(true)

        disposable.clear()

        camera.clearFrameProcessors()
    }
}
