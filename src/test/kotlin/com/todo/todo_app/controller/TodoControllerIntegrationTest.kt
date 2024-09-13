package com.todo.todo_app.controller

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.todo.todo_app.data.model.TodoCreateRequest
import com.todo.todo_app.data.model.TodoDto
import com.todo.todo_app.data.model.TodoUpdateRequest
import com.todo.todo_app.exception.TodoNotFoundException
import com.todo.todo_app.service.TodoService
import org.junit.jupiter.api.BeforeEach

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import java.time.LocalDateTime
import java.util.UUID
import kotlin.test.todo

@ExtendWith(SpringExtension::class)
@WebMvcTest(controllers = [TodoController::class])
class TodoControllerIntegrationTest(@Autowired private val mockMvc: MockMvc) {

    @MockBean
    private lateinit var mockService: TodoService

    private val todoId: UUID = UUID.fromString("801f7917-5650-4f7b-904f-0b3ed17d24f5")
    private val dummyTodoDto = TodoDto(
        id = todoId,
        createdAt =  LocalDateTime.now(),
        updatedAt =  LocalDateTime.now(),
        title = "play games",
        description = "lots of games",
        rank = 2,
        done = true
        )

    private val mapper = jacksonObjectMapper()

    @BeforeEach
    fun setUp() {
        mapper.registerModule(JavaTimeModule())
    }

    @Test
    fun `given get all todos endpoint called, check for number of todos`() {
        val todoDto = TodoDto(
            id = UUID.fromString("401cb1e7-7911-4fa0-b740-fac9eb24bbe6"),
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now(),
            title = "play even more games",
            description = "huge amount of games",
            rank = 3,
            done = false
        )

        val todos = listOf(dummyTodoDto, todoDto)

        `when`(mockService.getAllTodos()).thenReturn(listOf(dummyTodoDto, todoDto))
        val resultActions: ResultActions = mockMvc.perform(MockMvcRequestBuilders.get("/api/todos"))

        resultActions.andExpect(MockMvcResultMatchers.status().isOk)
        resultActions.andExpect(content().contentType(MediaType.APPLICATION_JSON))
        resultActions.andExpect(jsonPath("$.size()").value(todos.size))
    }

    @Test
    fun `when todo id does not exist in then we get is not found exception` () {
        `when`(mockService.getTodoById(todoId)).thenThrow(TodoNotFoundException("Todo with specified ID: $todoId does not exist."))
        val resultActions: ResultActions = mockMvc.perform(MockMvcRequestBuilders.get("/api/todos/$todoId"))

        resultActions.andExpect(MockMvcResultMatchers.status().isNotFound)
    }

    @Test
    fun `when a task by id is called without UUID in url expect bad request` () {
        val resultActions: ResultActions = mockMvc.perform(MockMvcRequestBuilders.get("/api/todos/hej"))

        resultActions.andExpect(MockMvcResultMatchers.status().isBadRequest)
    }

    @Test
    fun `when update todo is called then check for correct properties` () {
        val request = TodoUpdateRequest(
            title =  dummyTodoDto.title,
            description = dummyTodoDto.description,
            done = dummyTodoDto.done,
            rank = dummyTodoDto.rank
        )

        `when`(mockService.updateTodo(UUID.fromString("801f7917-5650-4f7b-904f-0b3ed17d24f5"), request)).thenReturn(dummyTodoDto)

        val resultActions: ResultActions = mockMvc.perform(
            MockMvcRequestBuilders.put
                ("/api/todos/${dummyTodoDto.id}"
                        ).contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request))
        )

        resultActions.andExpect(MockMvcResultMatchers.status().isOk)
        resultActions.andExpect(content().contentType(MediaType.APPLICATION_JSON))
        resultActions.andExpect(jsonPath("$.title").value(dummyTodoDto.title))
        resultActions.andExpect(jsonPath("$.description").value(dummyTodoDto.description))
        resultActions.andExpect(jsonPath("$.rank").value(dummyTodoDto.rank))
        resultActions.andExpect(jsonPath("$.done").value(dummyTodoDto.done))

    }

    @Test
    fun `given create task request when task is created then check for the properties`() {
        val request = TodoCreateRequest(
            title =  dummyTodoDto.title,
            description = dummyTodoDto.description,
            done = dummyTodoDto.done,
            rank = dummyTodoDto.rank
        )

        `when`(mockService.createTodo(request)).thenReturn(dummyTodoDto)
        val resultActions: ResultActions = mockMvc.perform(
            MockMvcRequestBuilders.post("/api/todos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request))
        )

        resultActions.andExpect(MockMvcResultMatchers.status().isOk)
        resultActions.andExpect(content().contentType(MediaType.APPLICATION_JSON))
        resultActions.andExpect(jsonPath("$.title").value(dummyTodoDto.title))
        resultActions.andExpect(jsonPath("$.description").value(dummyTodoDto.description))
        resultActions.andExpect(jsonPath("$.done").value(dummyTodoDto.done))
    }

    @Test
    fun `given id for delete request, check for message after deletion` () {
        val expectedMessage = "Todo with ID: $todoId was deleted."

        `when`(mockService.deleteTodo(todoId)).thenReturn(expectedMessage)
        val resultActions: ResultActions = mockMvc.perform(MockMvcRequestBuilders.delete("/api/todos/$todoId"))

        resultActions.andExpect(MockMvcResultMatchers.status().isOk)
        resultActions.andExpect(content().string(expectedMessage))
    }
}