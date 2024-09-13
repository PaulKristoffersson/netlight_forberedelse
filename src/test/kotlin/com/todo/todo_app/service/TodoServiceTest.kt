package com.todo.todo_app.service

import com.todo.todo_app.data.Todo
import com.todo.todo_app.data.model.TodoCreateRequest
import com.todo.todo_app.repository.TodoRepository
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.BeforeEach

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class TodoServiceTest {

    @RelaxedMockK
    private lateinit var mockKRepository: TodoRepository

    @InjectMockKs
    private lateinit var objectUnderTest: TodoService

    private val todo = Todo()
    private lateinit var createRequest: TodoCreateRequest

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
        createRequest = TodoCreateRequest(
            "Test",
            "Testing description",
            1,
            true
        )

    }


}