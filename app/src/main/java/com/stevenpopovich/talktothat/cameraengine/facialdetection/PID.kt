package com.stevenpopovich.talktothat.cameraengine.facialdetection

/**
 * PID library, ported from [https://github.com/br3ttb/Arduino-PID-Library] on [https://github.com/egueli/Kotlin-PID-Library].
 *
 * @param process an instance of the [Process] interface that holds the input,
 * setpoint and output values.
 * @param Kp the proportional term of the PID control. Can be changed with [setTunings].
 * @param Ki the integral term of the PID control. Can be changed with [setTunings].
 * @param Kd the derivative term of the PID control. Can be changed with [setTunings].
 * @param controllerDirection the controller direction. See [ControllerDirection] for details.
 * @param timeFunction the function that tells the current time. If specified, the lambda must return a never-decreasing number.
 */
class PID
@JvmOverloads constructor(
    private val process: Process,
    Kp: Double,
    Ki: Double,
    Kd: Double,
    private val controllerDirection: ControllerDirection = ControllerDirection.DIRECT,
    private val timeFunction: () -> Long = { System.currentTimeMillis() }
) {

    private var lastTime: Long = 0
    private var lastInput: Double = 0.0
    private var lastOutput: Double = 0.0
    private var outputSum: Double = 0.0
    private var sampleTime: Long = 100

    private var kp: Double = 0.0
    private var ki: Double = 0.0
    private var kd: Double = 0.0

    private var outMax: Double = 0.0
    private var outMin: Double = 0.0
    private var dispKp: Double = 0.0
    private var dispKi: Double = 0.0
    private var dispKd: Double = 0.0

    init {
        setOutputLimits(0.0, 255.0)
        setTunings(Kp, Ki, Kd)
        lastTime = invokeTimeFunctionForMillis() - sampleTime
        initialize()
    }

    /**
     * This, as they say, is where the magic happens.  This function should be called
     * as often as possible, or at least as fast as the sample time specified in
     * [setSampleTime]. The function will decide for itself whether a new
     * PID output needs to be computed. It will read [Process.input] and [Process.setpoint]
     * and will write the output to [Process.output].
     * @return true when the output is computed, false when nothing has been done.
     **/
    fun compute(): Boolean {
        val now = invokeTimeFunctionForMillis()
        val timeChange = now - lastTime
        if (timeChange < sampleTime) return false

        /*Compute all the working error variables*/
        val error = process.setpoint - process.input
        val dInput = process.input - lastInput
        outputSum += ki * error

        if (outputSum > outMax) outputSum = outMax
        if (outputSum < outMin) outputSum = outMin

        var output = kp * error

        /*Compute Rest of PID Output*/
        output += outputSum - kd * dInput

        if (output > outMax) output = outMax
        if (output < outMin) output = outMin
        process.output = output
        lastOutput = output

        /*Remember some variables for next time*/
        lastInput = process.input
        lastTime = now
        return true
    }

    /**
     * This function allows the controller's dynamic performance to be adjusted.
     * it's called automatically from the constructor, but tunings can also
     * be adjusted on the fly during normal operation.
     */
    @JvmOverloads
    private fun setTunings(Kp: Double, Ki: Double, Kd: Double) {
        if (Kp < 0 || Ki < 0 || Kd < 0) throw IllegalArgumentException("Kp, Ki and Kd must be non-negative")

        dispKp = Kp
        dispKi = Ki
        dispKd = Kd

        val sampleTimeInSec = sampleTime / 1000.0
        kp = Kp
        ki = Ki * sampleTimeInSec
        kd = Kd / sampleTimeInSec

        if (controllerDirection == ControllerDirection.REVERSE) {
            kp = -kp
            ki = -ki
            kd = -kd
        }
    }

    /**
     * Set the minimum/maximum values that can be present at the output. If the
     * current output is outside these values, it will be clamped.
     */
    fun setOutputLimits(min: Double, max: Double) {
        if (min >= max) throw IllegalArgumentException("maximum output must be higher than the minimum output")

        outMin = min
        outMax = max

        if (lastOutput > outMax) process.output = outMax
        else if (lastOutput < outMin) process.output = outMin

        if (outputSum > outMax) outputSum = outMax
        else if (outputSum < outMin) outputSum = outMin
    }

    private fun initialize() {
        outputSum = process.output
        lastInput = process.input
        if (outputSum > outMax) outputSum = outMax
        else if (outputSum < outMin) outputSum = outMin
    }

    private fun invokeTimeFunctionForMillis() = timeFunction.invoke()
}

enum class ControllerMode { MANUAL, AUTOMATIC }

/**
 * Describes the direction of the controller, i.e. the sign of the output.
 */
enum class ControllerDirection {
    /**
     * A positive number in the output will cause eventually an increase of the
     * input.
     */
    DIRECT,
    /**
     * A positive number in the output will cause eventually a decrease of the
     * input.
     */
    REVERSE
}

/**
 * Provides the link to the process to be controlled by a [PID].
 */
interface Process {
    /**
     * The input to be fed to the controller, i.e. the current state of the process.
     * This must be updated before calling [PID.compute].
     */
    var input: Double

    /**
     * The output of the controller, i.e. the process parameter to control.
     *
     * Once [PID.compute] is called, read this value to set the process to that
     * value.
     *
     * Please keep this value updated when the process is controlled externally,
     * i.e. while the controller is in [ControllerMode.MANUAL] mode. This allows
     * for a smooth engagement when the controller is set to [ControllerMode.AUTOMATIC].
     */
    var output: Double

    /**
     * The desired state of the process. The controller will try to change
     * [output] so that [input] is equal to this value.
     */
    var setpoint: Double
}
