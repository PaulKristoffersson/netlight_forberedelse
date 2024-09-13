package com.todo.todo_app.service

import com.todo.todo_app.data.Todo
import com.todo.todo_app.data.model.TodoCreateRequest
import com.todo.todo_app.data.model.TodoDto
import com.todo.todo_app.exception.BadRequestException
import com.todo.todo_app.exception.TodoNotFoundException
import com.todo.todo_app.repository.TodoRepository
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.http.HttpStatus
import java.util.*

@ExtendWith(MockKExtension::class)
class TodoServiceTest {

    @RelaxedMockK
    private lateinit var mockKRepository: TodoRepository

    @InjectMockKs
    private lateinit var objectUnderTest: TodoService

    private val todo = Todo()
    private lateinit var createRequest: TodoCreateRequest
    private val todoId = UUID.fromString("801f7917-5650-4f7b-904f-0b3ed17d24f5")

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
        createRequest = TodoCreateRequest(
            title = "Testar",
            description = "Testing description",
            rank = 1,
            done = true
        )
    }

    @Test
    fun `after fetching all todos check the number of records is correct`() {
        val expectedTodos: List<Todo> = listOf(Todo(), Todo())

        every { mockKRepository.queryAllByRank()} returns expectedTodos.toMutableList()
        val actualTodos: List<TodoDto> = objectUnderTest.getAllTodos()
        assertThat(actualTodos.size).isEqualTo(expectedTodos.size)
    }

    @Test
    fun `when todo is created check for its properties`() {
        todo.title = createRequest.title
        todo.description = createRequest.description
        todo.rank = createRequest.rank
        todo.done = createRequest.done

        every { mockKRepository.save(any()) } returns todo
        val actualTodoDto: TodoDto = objectUnderTest.createTodo(createRequest)

        assertThat(actualTodoDto.title).isEqualTo(todo.title)
        assertThat(actualTodoDto.description).isEqualTo(todo.description)
        assertThat(actualTodoDto.rank).isEqualTo(todo.rank)
        assertThat(actualTodoDto.done).isEqualTo(todo.done)

    }

    @Test
    fun `when todo title exists check for bad request exception`() {
        every { mockKRepository.doesTitleExist(any()) } returns true
        val exception = assertThrows<BadRequestException> {objectUnderTest.createTodo(createRequest)}

        assertThat(exception.message).isEqualTo("Title with description: ${createRequest.title}, already exists.")
    }

    @Test
    fun `when get todo with incorrect id is called throw exception`() {
        every { mockKRepository.existsById(any()) } returns false
        val exception: TodoNotFoundException = assertThrows {objectUnderTest.getTodoById(todoId)}
        assertThat(exception.message).isEqualTo("Todo with specified ID: $todoId does not exist.")
    }

    @Test
    fun `when all completed todos are fetched check the property if it is complete`(){
        todo.done = true
        val expectedTodos = listOf(todo)

        every { mockKRepository.queryAllCompletedTodos() } returns expectedTodos.toMutableList()
        val actualList: List<TodoDto> = objectUnderTest.getAllCompletedTodos()

        assertThat(actualList[0].done).isEqualTo(expectedTodos[0].done)
    }

    @Test
    fun `when all uncompleted todos are fetched check the property if it is uncomplete`(){
        todo.done = false
        val expectedTodos = listOf(todo)

        every { mockKRepository.queryAllUncompletedTodos() } returns expectedTodos.toMutableList()
        val actualList: List<TodoDto> = objectUnderTest.getAllUncompletedTodos()

        assertThat(actualList[0].done).isEqualTo(expectedTodos[0].done)
    }
}