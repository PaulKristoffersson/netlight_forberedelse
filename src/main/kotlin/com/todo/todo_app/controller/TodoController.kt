package com.todo.todo_app.controller

import com.todo.todo_app.data.model.TodoCreateRequest
import com.todo.todo_app.data.model.TodoDto
import com.todo.todo_app.data.model.TodoUpdateRequest
import com.todo.todo_app.service.TodoService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("api")
class TodoController (private val service: TodoService) {

    @GetMapping("todos")
    fun getAllTodos(): ResponseEntity<List<TodoDto>> = ResponseEntity(service.getAllTodos(), HttpStatus.OK)

    @GetMapping("todos/{id}")
    fun getTodoById(@PathVariable id: UUID): ResponseEntity<TodoDto> =
        ResponseEntity(service.getTodoById(id), HttpStatus.OK)

    @PostMapping("todos")
    fun createTodo(@Valid @RequestBody request: TodoCreateRequest): ResponseEntity<TodoDto> =
        ResponseEntity(service.createTodo(request), HttpStatus.OK)

    @PutMapping("todos/{id}")
    fun updateTodo(
        @PathVariable id: UUID,
        @Valid @RequestBody request: TodoUpdateRequest
    ): ResponseEntity<TodoDto> = ResponseEntity(service.updateTodo(id, request), HttpStatus.OK)

    @DeleteMapping("todos/{id}")
    fun deleteTodo(@PathVariable id: UUID): ResponseEntity<String> = ResponseEntity(service.deleteTodo(id), HttpStatus.OK)


}