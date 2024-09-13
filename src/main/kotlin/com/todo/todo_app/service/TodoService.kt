package com.todo.todo_app.service

import com.todo.todo_app.data.Todo
import com.todo.todo_app.data.model.TodoCreateRequest
import com.todo.todo_app.data.model.TodoDto
import com.todo.todo_app.data.model.TodoUpdateRequest
import com.todo.todo_app.exception.BadRequestException
import com.todo.todo_app.exception.TodoNotFoundException
import com.todo.todo_app.repository.TodoRepository
import org.springframework.scheduling.config.Task
import org.springframework.stereotype.Service
import org.springframework.util.ReflectionUtils
import java.lang.reflect.Field
import java.time.LocalDateTime
import java.util.*
import java.util.stream.Collectors
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.javaField

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
        todo.title = request.title
        todo.description = request.description
        todo.rank = request.rank
        todo.done = request.done
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

    fun createTodo(request: TodoCreateRequest): TodoDto {
        if (repository.doesTitleExist(request.title)) {
            throw BadRequestException("Title with description: ${request.title}, already exists.")
        }
        val todo = Todo()
        mappingFromRequestToEntity(todo, request)
        val savedTodo: Todo = repository.save(todo)
        return mappingEntityToDto(savedTodo)
    }

    fun updateTodo(id: UUID, request: TodoUpdateRequest): TodoDto {
        checkTodoForId(id)
        val exisitingTodo = repository.findTodoById(id)

        for (prop in TodoUpdateRequest::class.memberProperties) {
            if (prop.get(request) != null) {
                val field: Field? = ReflectionUtils.findField(Task::class.java, prop.name)
                field?.let {
                    it.isAccessible = true
                    ReflectionUtils.setField(it, exisitingTodo, prop.get(request))
                }
            }
        }
        exisitingTodo.updatedAt = LocalDateTime.now()
        val savedTodo: Todo = repository.save(exisitingTodo)

        return mappingEntityToDto(savedTodo)
    }

    fun deleteTodo(id: UUID): String {
        checkTodoForId(id)
        repository.deleteById(id)
        return "Todo with ID: $id was deleted."
    }
}