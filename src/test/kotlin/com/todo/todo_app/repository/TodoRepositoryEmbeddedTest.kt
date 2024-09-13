package com.todo.todo_app.repository

import com.todo.todo_app.data.Todo
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.jdbc.Sql
import java.util.*

@DataJpaTest(properties =  ["spring.jpa.properties.javax.persistence.validation.mode=none"])
class TodoRepositoryEmbeddedTest {

    @Autowired
    private lateinit var objectUnderTest: TodoRepository

    private val numberOfRecordsInTestData = 3
    private val numberOfCompletedRecordsInTestData = 2
    private val numberOfUncompletedRecordsInTestData = 1

    @Test
    @Sql("classpath:test-data.sql")
    fun `Post saving todos, check for null values`() {
        val todo: Todo = objectUnderTest.findTodoById(UUID.fromString("401cb1e7-7911-4fa0-b740-fac9eb24bbe6"))

        assertThat(todo).isNotNull

    }

    @Test
    @Sql("classpath:test-data.sql")
    fun `Post saving all todos, check for the number of records in db`() {
        val todos: List<Todo> = objectUnderTest.findAll()

        assertThat(todos.size).isEqualTo(numberOfRecordsInTestData)
    }

    @Test
    @Sql("classpath:test-data.sql")
    fun `Post saving all todos, check for the number of records that are completed in db`() {
        val todos: List<Todo> = objectUnderTest.queryAllCompletedTodos()

        assertThat(todos.size).isEqualTo(numberOfCompletedRecordsInTestData)
    }

    @Test
    @Sql("classpath:test-data.sql")
    fun `Post saving all todos, check for the number of records that are uncompleted in db`() {
        val todos: List<Todo> = objectUnderTest.queryAllUncompletedTodos()

        assertThat(todos.size).isEqualTo(numberOfUncompletedRecordsInTestData)
    }

    @Test
    @Sql("classpath:test-data.sql")
    fun `After deleting todo, check number of records in db`() {
        objectUnderTest.deleteById(UUID.fromString("801f7917-5650-4f7b-904f-0b3ed17d24f5"))
        val todos: List<Todo> = objectUnderTest.findAll()
        assertThat(todos.size).isEqualTo(2)
    }

    @Test
    @Sql("classpath:test-data.sql")
    fun `After deleting completed todo, check number of completed records in db`() {
        objectUnderTest.deleteById(UUID.fromString("801f7917-5650-4f7b-904f-0b3ed17d24f5"))
        val todos: List<Todo> = objectUnderTest.queryAllCompletedTodos()
        assertThat(todos.size).isEqualTo(1)
    }

    @Test
    @Sql("classpath:test-data.sql")
    fun `when looking for title that already exists, check that we get message`() {
        val doesTitleExist = objectUnderTest.doesTitleExist("third todo")
        val doesTitleNotExist = objectUnderTest.doesTitleExist("not exist")
        assertThat(doesTitleExist).isTrue
        assertThat(doesTitleNotExist).isFalse
    }


}