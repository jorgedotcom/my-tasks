package com.jorgedotcom.mytasks.domain.model

import com.jorgedotcom.mytasks.data.db.TaskEntity

data class Task(
    val id: Int? = null,
    val title: String = "",
    val description: String = "",
    val date: String? = null
)

fun Task.toTaskEntity(): TaskEntity {
    return TaskEntity(
        id = id,
        title = title,
        description = description,
        date = date
    )
}
