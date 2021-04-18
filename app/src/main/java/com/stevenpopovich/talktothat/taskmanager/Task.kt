package com.stevenpopovich.talktothat.taskmanager

interface Task {
    fun iterate()

    val isComplete: Boolean
}
