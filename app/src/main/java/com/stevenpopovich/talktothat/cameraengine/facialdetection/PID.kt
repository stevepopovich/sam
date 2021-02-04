package com.stevenpopovich.talktothat.cameraengine.facialdetection

/**
 * PID library, ported from [https://github.com/br3ttb/Arduino-PID-Library] on [https://github.com/egueli/Kotlin-PID-Library].
 *
 * At startup, it is in manual mode. It can go
 * into effective operation after calling [setMode] with [ControllerMode.AUTOMATIC].
 *
 * @param process an instance of the [Process] interface that holds the input,
 * setpoint and output values.
 * @param Kp the proportional term of the PID control. Can be changed with [setTunings].
 * @param Ki the integral term of the PID control. Can be changed with [setTunings].
 * @param Kd the derivative term of the PID control. Can be changed with [setTunings].
 * @param proportionalOn tells at which part the proportional term should be applied to. See [ProportionalOn] for details.
 * @param controllerDirection the controller direction. See [ControllerDirection] for details.
 * @param timeFunction the function that tells the current time. If specified, the lambda must return a never-decreasing number.
 */
class PID
@JvmOverloads constructor(
    private val process: Process,
    Kp: Double,
    Ki: Double,
    Kd: Double,
    controllerDirection: ControllerDirection,
    private var proportionalOn: ProportionalOn = ProportionalOn.ERROR,
    private var timeFunction: () -> Long = { System.currentTimeMillis() }
) {
    private var inAuto: Boolean = true
    private var lastTime: Long = 0
    private var sampleTime: Long = 100
    private var lastInput: Double = 0.0
    private var lastOutput: Double = 0.0
    private var outputSum: Double = 0.0
    private var kp: Double = 0.0
    private var ki: Double = 0.0
    private var kd: Double = 0.0
    private lateinit var controllerDirection: ControllerDirection
    private var outMax: Double = 0.0
    private var outMin: Double = 0.0
    private var dispKp: Double = 0.0
    private var dispKi: Double = 0.0
    private var dispKd: Double = 0.0

    init {
        setOutputLimits(0.0, 255.0)
        setControllerDirection(controllerDirection)
        setTunings(Kp, Ki, Kd, proportionalOn)
        lastTime = millis() - sampleTime
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
        if (!inAuto) return false

        val now = millis()
        val timeChange = now - lastTime
        if (timeChange < sampleTime) return false

        /*Compute all the working error variables*/
        val error = process.setpoint - process.input
        val dInput = process.input - lastInput
        outputSum += ki * error

        /*Add Proportional on Measurement, if P_ON_M is specified*/
        if (proportionalOn == ProportionalOn.MEASUREMENT) {
            outputSum -= kp * dInput
        }

        if (outputSum > outMax) outputSum = outMax
        if (outputSum < outMin) outputSum = outMin

        /*Add Proportional on Error, if P_ON_E is specified*/
        var output = if (proportionalOn == ProportionalOn.ERROR) {
            kp * error
        } else {
            0.0
        }

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
    fun setTunings(Kp: Double, Ki: Double, Kd: Double, proportionalOn: ProportionalOn = this.proportionalOn) {
        if (Kp < 0 || Ki < 0 || Kd < 0) throw IllegalArgumentException("Kp, Ki and Kd must be non-negative")

        this.proportionalOn = proportionalOn

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
     * sets the period, in Milliseconds, at which the calculation is performed
     */
    fun setSampleTime(newSampleTime: Long) {
        if (newSampleTime <= 0) throw IllegalArgumentException("sample time must be higher than zero")

        val ratio = newSampleTime.toDouble() / sampleTime
        ki *= ratio
        kd /= ratio
        sampleTime = newSampleTime
    }

    /**
     * Set the minimum/maximum values that can be present at the output. If the
     * current output is outside these values, it will be clamped.
     */
    fun setOutputLimits(min: Double, max: Double) {
        if (min >= max) throw IllegalArgumentException("maximum output must be higher than the minimum output")

        outMin = min
        outMax = max

        if (inAuto) {
            if (lastOutput > outMax) process.output = outMax
            else if (lastOutput < outMin) process.output = outMin

            if (outputSum > outMax) outputSum = outMax
            else if (outputSum < outMin) outputSum = outMin
        }
    }

    /**
     * Allows the controller Mode to be set to manual or Automatic (non-zero)
     * when the transition from manual to auto occurs, the controller is
     * automatically initialized
     */
    fun setMode(mode: ControllerMode) {
        val newAuto = (mode == ControllerMode.AUTOMATIC)
        if (newAuto && !inAuto) { /*we just went from manual to auto*/
            initialize()
        }
        inAuto = newAuto
    }

    fun initialize() {
        outputSum = process.output
        lastInput = process.input
        if (outputSum > outMax) outputSum = outMax
        else if (outputSum < outMin) outputSum = outMin
    }

    /**
     * The PID will either be connected to a DIRECT acting process (+Output leads
     * to +Input) or a REVERSE acting process(+Output leads to -Input.)  we need to
     * know which one, because otherwise we may increase the output when we should
     * be decreasing.  This is called from the constructor.
     */
    fun setControllerDirection(direction: ControllerDirection) {
        if (inAuto && direction != controllerDirection) {
            kp = -kp
            ki = -ki
            kd = -kd
        }
        this.controllerDirection = direction
    }

    fun getKp() = dispKp
    fun getKi() = dispKi
    fun getKd() = dispKd
    fun getMode() = if (inAuto) ControllerMode.AUTOMATIC else ControllerMode.MANUAL

    private fun millis() = timeFunction.invoke()
}

enum class ProportionalOn { MEASUREMENT, ERROR }
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
