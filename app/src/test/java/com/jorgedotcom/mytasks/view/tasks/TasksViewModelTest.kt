package com.jorgedotcom.mytasks.view.tasks

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.jorgedotcom.mytasks.data.repository.ITasksRepository
import com.jorgedotcom.mytasks.domain.model.Task
import com.jorgedotcom.mytasks.getOrAwaitValue
import io.mockk.every
import io.mockk.mockk
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class TasksViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var tasksRepository: ITasksRepository
    private lateinit var tasksViewModel: TasksViewModel
    private val tasks = listOf(
        Task(id = 1, title = "Title 1", description = "Description 1", date = "Date 1"),
        Task(id = 2, title = "Title 2", description = "Description 2", date = "Date 2"),
        Task(id = 3, title = "Title 3", description = "Description 3", date = "Date 3")
    )

    @Before
    fun setUp() {
        tasksRepository = mockk<ITasksRepository>().also {
            every { it.getAllTasksLiveData() } returns MutableLiveData(tasks)
        }
        tasksViewModel = TasksViewModel(tasksRepository)
    }

    @Test
    fun init_checkLiveData() {
        // Given
        val uiState = TasksUiState(tasks)

        // When
        val result = tasksViewModel.tasksUiState.getOrAwaitValue()

        // Then
        assertThat(result, `is`(uiState))
    }
}
