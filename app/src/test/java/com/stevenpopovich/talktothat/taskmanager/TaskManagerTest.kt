package com.stevenpopovich.talktothat.taskmanager

import com.stevenpopovich.talktothat.taskmanager.tasks.Task
import com.stevenpopovich.talktothat.testutils.relaxedMock
import io.mockk.confirmVerified
import io.mockk.verify
import io.mockk.verifySequence
import org.junit.Test

class TaskManagerTest {

    private val currentTask: Task = relaxedMock()

    private lateinit var taskManager: TaskManager

    @Test
    fun `stop calls finish on the current task and sets it to null`() {
        taskManager = TaskManager(currentTask)

        taskManager.stop()

        verify { currentTask.finish() }

        confirmVerified(currentTask)
    }

    @Test
    fun `runTask sets the current task if null and calls start on it`() {
        taskManager = TaskManager()

        taskManager.runTask(currentTask)
        taskManager.runTask(currentTask)

        verifySequence { currentTask.start() }

        confirmVerified(currentTask)
    }
}
