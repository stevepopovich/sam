package com.stevenpopovich.talktothat.taskmanager

import com.stevenpopovich.talktothat.usbinterfacing.ArduinoInterface
import java.time.Instant.now
import java.util.concurrent.TimeUnit
import kotlin.random.Random

class LookAtMeTask(
    arduinoInterface: ArduinoInterface
) : Task {

    private val faceDetectionTime = Random.nextInt(5, 15) to TimeUnit.SECONDS
    private val faceFindingTimeout = 5 to TimeUnit.SECONDS
    private val startTime = now()

    private var currTime = now()

    override fun iterate() {
        currTime = now()
    }

    override val isComplete: Boolean
        get() = TODO("Not yet implemented")
}

private enum class LookAtMeState {
    FINDING_FACE, TRACKING_FACE
}
