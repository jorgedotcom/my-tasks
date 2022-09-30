package com.jorgedotcom.mytasks.view.new_task

data class NewTaskUiState(
    val formattedDate: String? = null,
    val taskTitle: String = "",
    val taskDescription: String = "",
    val shouldNavBack: Boolean = false,
    val isEdit: Boolean = false
)
