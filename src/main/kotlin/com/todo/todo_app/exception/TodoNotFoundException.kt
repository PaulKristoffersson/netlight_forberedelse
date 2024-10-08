package com.todo.todo_app.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.NOT_FOUND)
data class TodoNotFoundException(override val message: String): RuntimeException()
