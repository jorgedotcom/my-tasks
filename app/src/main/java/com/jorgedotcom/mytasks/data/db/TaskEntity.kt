package com.jorgedotcom.mytasks.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.jorgedotcom.mytasks.domain.model.Task

@Entity
data class TaskEntity(
    @PrimaryKey val id: Int? = null,
    val title: String?,
    val description: String?,
    val date: String?
)

fun TaskEntity.toTask(): Task {
    return Task(
        id = id ?: DEFAULT_TASK_ID,
        title = title ?: "",
        description = description ?: "",
        date = date ?: ""
    )
}

const val DEFAULT_TASK_ID = -1
