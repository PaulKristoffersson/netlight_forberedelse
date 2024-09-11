package com.todo.todo_app.data.model

import java.time.LocalDateTime

data class TodoDto(
    val id: Long,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val title: String,
    val description: String,
    val rank: Int,
    val done: Boolean,
)
