package com.todo.todo_app.data.model

import jakarta.validation.constraints.NotBlank
import java.time.LocalDateTime
import java.util.UUID

data class TodoCreateRequest(
    @NotBlank(message = "Title cannot be blank")
    val title: String,

    val description: String,

    val rank: Int,

    val done: Boolean,
)
