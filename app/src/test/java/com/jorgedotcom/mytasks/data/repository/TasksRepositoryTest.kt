package com.jorgedotcom.mytasks.data.repository

import com.jorgedotcom.mytasks.data.db.TaskEntity
import com.jorgedotcom.mytasks.data.db.TasksDao
import com.jorgedotcom.mytasks.data.db.toTask
import com.jorgedotcom.mytasks.domain.model.Task
import com.jorgedotcom.mytasks.domain.model.toTaskEntity
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TasksRepositoryTest {

    private lateinit var tasksDao: TasksDao
    private lateinit var tasksRepository: ITasksRepository

    @Before
    fun setUp() {
        // Normally, this would be a real implementation using an in-memory database
        tasksDao = mockk()
        tasksRepository = TasksRepository(tasksDao)
    }

    @Test
    fun createTask_verifyTasksDaoCall() = runTest {
        // Given
        val task = Task(title = "Title", description = "Description", date = "7/11/2022")
        // Either provide a return object or make the mock relaxed: 'mockk(relaxed = true)'
        coEvery { tasksDao.addTask(any()) } returns Unit

        // When
        tasksRepository.createTask(task)

        // Then
        coVerify {
            tasksDao.addTask(task.toTaskEntity())
        }
    }

    @Test
    fun getAllTasksFlow_checkResult() = runTest {
        // Given
        val tasks = listOf(
            TaskEntity(id = 1, title = "Title 1", description = "Description 1", date = "Date 1"),
            TaskEntity(id = 2, title = "Title 2", description = "Description 2", date = "Date 2"),
            TaskEntity(id = 3, title = "Title 3", description = "Description 3", date = "Date 3")
        )
        every { tasksDao.getTasksStateFlow() } returns flowOf(tasks)

        // When
        val result = tasksRepository.getAllTasksFlow().first()

        // Then
        assertThat(result, `is`(tasks.map { it.toTask() }))
    }
}
