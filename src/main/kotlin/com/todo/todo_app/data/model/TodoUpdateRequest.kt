package com.todo.todo_app.data.model

import java.time.LocalDateTime
import java.util.*

data class TodoUpdateRequest(
    val updatedAt: LocalDateTime,
    val title: String?,
    val description: String?,
    val rank: Int?,
    val done: Boolean?,
)
