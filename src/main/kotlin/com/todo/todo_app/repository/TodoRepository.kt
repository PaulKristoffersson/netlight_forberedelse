package com.todo.todo_app.repository

import com.todo.todo_app.data.Todo
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface TodoRepository : JpaRepository <Todo, UUID> {


    fun findTodoById(id: UUID): Todo

    @Query(value = "SELECT * FROM todo ORDER BY rank", nativeQuery = true)
    fun queryAllByRank(): List<Todo>

    @Query("SELECT CASE WHEN COUNT(t) > 0 THEN TRUE ELSE FALSE END FROM Todo t WHERE t.title =? 1")
    fun doesTitleExist(title: String): Boolean
}