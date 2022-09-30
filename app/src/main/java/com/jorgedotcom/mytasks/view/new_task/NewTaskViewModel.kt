package com.jorgedotcom.mytasks.view.new_task

import android.annotation.SuppressLint
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jorgedotcom.mytasks.data.db.DEFAULT_TASK_ID
import com.jorgedotcom.mytasks.data.repository.ITasksRepository
import com.jorgedotcom.mytasks.domain.model.Task
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@SuppressLint("CheckResult")
@HiltViewModel
class NewTaskViewModel @Inject constructor(
    private val tasksRepository: ITasksRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private var currentTaskId: Int? = null
    private var _newTaskUiState = MutableStateFlow(NewTaskUiState())
    val newTaskUiState: StateFlow<NewTaskUiState>
        get() = _newTaskUiState.asStateFlow()

    init {
        savedStateHandle.get<Int>("taskId")?.let { taskId ->
            currentTaskId = taskId.takeIf { it != DEFAULT_TASK_ID }
            tasksRepository.getTaskFlow(taskId)
                .onEach { task ->
                    task?.let {
                        _newTaskUiState.value = newTaskUiState.value.copy(
                            formattedDate = it.date,
                            taskTitle = it.title,
                            taskDescription = it.description,
                            isEdit = true
                        )
                    }
                }
                .launchIn(viewModelScope)
        }
    }

    fun addEditTask() {
        newTaskUiState.value.let {
            if (it.taskTitle.isNotEmpty()
                && it.taskDescription.isNotEmpty()
                && it.formattedDate != null
            ) {
                val task = Task(
                    id = currentTaskId,
                    title = it.taskTitle,
                    description = it.taskDescription,
                    date = it.formattedDate
                )
                viewModelScope.launch {
                    if (newTaskUiState.value.isEdit) {
                        tasksRepository.updateTask(task)
                    } else {
                        tasksRepository.createTask(task)
                    }
                }
                _newTaskUiState.value = it.copy(shouldNavBack = true)
            } else {
                return@addEditTask
            }
        }
    }

    fun updateTaskTitle(title: String) {
        _newTaskUiState.value = newTaskUiState.value.copy(taskTitle = title)
    }

    fun updateTaskDescription(description: String) {
        _newTaskUiState.value = newTaskUiState.value.copy(taskDescription = description)
    }

    fun formatDate(epoch: Long) {
        val instant = Instant.ofEpochMilli(epoch)
        val date = LocalDateTime.ofInstant(instant, ZoneOffset.UTC)
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        _newTaskUiState.value = newTaskUiState.value.copy(formattedDate = formatter.format(date))
    }
}
