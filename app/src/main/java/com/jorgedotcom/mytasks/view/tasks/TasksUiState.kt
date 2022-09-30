package com.jorgedotcom.mytasks.view.tasks

import com.jorgedotcom.mytasks.domain.model.Task

data class TasksUiState(
    val tasks: List<Task> = listOf()
)
