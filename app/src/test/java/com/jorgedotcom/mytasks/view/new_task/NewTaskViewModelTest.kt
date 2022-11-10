package com.jorgedotcom.mytasks.view.new_task

import androidx.lifecycle.SavedStateHandle
import com.jorgedotcom.mytasks.data.repository.ITasksRepository
import com.jorgedotcom.mytasks.domain.model.Task
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.nullValue
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.emptyString
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class NewTaskViewModelTest {

    private lateinit var tasksRepository: ITasksRepository
    private lateinit var newTaskViewModel: NewTaskViewModel
    private val dispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        tasksRepository = mockk(relaxed = true)
        newTaskViewModel = NewTaskViewModel(tasksRepository, SavedStateHandle())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun init_checkIsEditTrue() {
        // Given
        val task = Task(id = 1, title = "Title", description = "Description", date = "Date")
        val savedStateHandle = SavedStateHandle().apply { set("taskId", task.id) }
        every { tasksRepository.getTaskFlow(task.id!!) } returns flowOf(task)
        newTaskViewModel = NewTaskViewModel(tasksRepository, savedStateHandle)

        // When
        val uiState = newTaskViewModel.newTaskUiState.value

        // Then
        assertThat(uiState.isEdit, `is`(true))
        assertThat(uiState.shouldNavBack, `is`(false))
        assertThat(uiState.taskTitle, `is`(task.title))
        assertThat(uiState.taskDescription, `is`(task.description))
        assertThat(uiState.formattedDate, `is`(task.date))
    }

    @Test
    fun init_checkIsEditFalse() {
        // When
        val uiState = newTaskViewModel.newTaskUiState.value

        // Then
        assertThat(uiState.isEdit, `is`(false))
        assertThat(uiState.shouldNavBack, `is`(false))
        assertThat(uiState.taskTitle, `is`(emptyString()))
        assertThat(uiState.taskDescription, `is`(emptyString()))
        assertThat(uiState.formattedDate, `is`(nullValue()))
    }

    @Test
    fun updateTaskTitle_updatesStateCorrectly() {
        // When
        newTaskViewModel.updateTaskTitle("Title")

        // Then
        val uiState = newTaskViewModel.newTaskUiState.value
        assertThat(uiState.taskTitle, `is`("Title"))
        assertThat(uiState.taskDescription, `is`(emptyString()))
        assertThat(uiState.formattedDate, `is`(nullValue()))
    }

    @Test
    fun updateTaskDescription_updatesStateCorrectly() {
        // When
        newTaskViewModel.updateTaskDescription("Description")

        // Then
        val uiState = newTaskViewModel.newTaskUiState.value
        assertThat(uiState.taskDescription, `is`("Description"))
        assertThat(uiState.taskTitle, `is`(emptyString()))
        assertThat(uiState.formattedDate, `is`(nullValue()))
    }

    @Test
    fun formatDate_updatesStateCorrectly() {
        // When
        newTaskViewModel.formatDate(-16804800000)

        // Then
        val uiState = newTaskViewModel.newTaskUiState.value
        assertThat(uiState.formattedDate, `is`("20/06/1969"))
        assertThat(uiState.taskTitle, `is`(emptyString()))
        assertThat(uiState.taskDescription, `is`(emptyString()))
    }

    @Test
    fun addEditTask_verifyCreateTask() {
        // Given
        val task = Task(title = "Title", description = "Description", date = "20/06/1969")
        newTaskViewModel.updateTaskTitle(task.title)
        newTaskViewModel.updateTaskDescription(task.description)
        newTaskViewModel.formatDate(-16804800000)

        // When
        newTaskViewModel.addEditTask()
        val uiState = newTaskViewModel.newTaskUiState.value

        // Then
        assertThat(uiState.shouldNavBack, `is`(true))
        coVerify { tasksRepository.createTask(task) }
    }

    @Test
    fun addEditTask_verifyCreateTaskWhenEmpty() {
        // When
        newTaskViewModel.addEditTask()
        val uiState = newTaskViewModel.newTaskUiState.value

        // Then
        assertThat(uiState.shouldNavBack, `is`(false))
    }

    @Test
    fun addEditTask_verifyEditTask() {
        // Given
        val task = Task(id = 1)
        val updatedTask = Task(
            id = 1,
            title = "New Title",
            description = "New Description",
            date = "20/06/1969"
        )

        val savedStateHandle = SavedStateHandle().apply { set("taskId", task.id) }
        every { tasksRepository.getTaskFlow(task.id!!) } returns flowOf(task)
        newTaskViewModel = NewTaskViewModel(tasksRepository, savedStateHandle)

        // When
        newTaskViewModel.updateTaskTitle("New Title")
        newTaskViewModel.updateTaskDescription("New Description")
        newTaskViewModel.formatDate(-16804800000)
        newTaskViewModel.addEditTask()
        val uiState = newTaskViewModel.newTaskUiState.value

        // Then
        assertThat(uiState.shouldNavBack, `is`(true))
        coVerify { tasksRepository.updateTask(updatedTask) }
    }
}
