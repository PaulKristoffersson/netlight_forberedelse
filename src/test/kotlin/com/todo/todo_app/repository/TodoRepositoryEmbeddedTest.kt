package com.todo.todo_app.repository

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest

@DataJpaTest(properties =  ["spring.jpa.properties.javax.persistence.validation.mode=none"])
class TodoRepositoryEmbeddedTest {

    @Autowired
    private lateinit var objectUnderTest: TodoRepository

    private val numberOfRecordsInTestData = 3
    private val numberOfCompletedRecordsInTestData = 2
    private val numberOfUncompletedRecordsInTestData = 1

}