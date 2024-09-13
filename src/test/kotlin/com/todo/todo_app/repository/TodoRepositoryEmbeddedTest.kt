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

}