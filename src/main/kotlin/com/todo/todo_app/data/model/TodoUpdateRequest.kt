package com.todo.todo_app.data.model

data class TodoUpdateRequest(
    val title: String?,
    val description: String?,
    val rank: Int?,
    val done: Boolean?,
)
