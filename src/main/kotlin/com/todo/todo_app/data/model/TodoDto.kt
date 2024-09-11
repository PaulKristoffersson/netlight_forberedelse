package com.todo.todo_app.data.model

import java.time.LocalDateTime
import java.util.UUID

data class TodoDto(
    val id: UUID?,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val title: String,
    val description: String,
    val rank: Int,
    val done: Boolean,
)
