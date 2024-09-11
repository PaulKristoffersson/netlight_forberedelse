package com.todo.todo_app.service

import com.todo.todo_app.data.Todo
import com.todo.todo_app.data.model.TodoCreateRequest
import com.todo.todo_app.data.model.TodoDto
import com.todo.todo_app.exception.TodoNotFoundException
import com.todo.todo_app.repository.TodoRepository
import org.springframework.scheduling.config.Task
import org.springframework.stereotype.Service
import java.util.*
import java.util.stream.Collectors

@Service
class TodoService(private val repository: TodoRepository) {

    private fun mappingEntityToDto(todo: Todo) = TodoDto(
            todo.id,
            todo.createdAt,
            todo.updatedAt,
            todo.title,
            todo.description,
            todo.rank,
            todo.done,
    )

    private fun mappingFromRequestToEntity(todo: Todo, request: TodoCreateRequest) {
        todo.updatedAt = request.updatedAt
        todo.title = request.title
        todo.description = request.description
        todo.rank = request.rank
    }

    private fun checkTodoForId(id: UUID) {
        if (!repository.existsById(id)) {
            throw TodoNotFoundException("Todo with specified ID: $id does not exist.")
        }
    }

    fun getTodoById(id: UUID): TodoDto {
        checkTodoForId(id)
        val todo: Todo = repository.findTodoById(id)
        return mappingEntityToDto(todo)
    }

    fun getAllTodos(): List<TodoDto> =
        repository.queryAllByRank().stream().map(this::mappingEntityToDto).collect(Collectors.toList())
}