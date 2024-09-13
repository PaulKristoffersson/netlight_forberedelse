package com.todo.todo_app.repository

import com.todo.todo_app.data.Todo
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface TodoRepository : JpaRepository <Todo, UUID> {


    fun findTodoById(id: UUID): Todo

    @Query(value = "SELECT * FROM todo ORDER BY rank", nativeQuery = true)
    fun queryAllByRank(): List<Todo>

    @Query(value = "SELECT * FROM todo WHERE done = TRUE", nativeQuery = true)
    fun queryAllCompletedTodos(): List<Todo>

    @Query(value = "SELECT * FROM todo WHERE done = FALSE", nativeQuery = true)
    fun queryAllUncompletedTodos(): List<Todo>

    @Query("SELECT CASE WHEN COUNT(t) > 0 THEN true ELSE false END FROM Todo t WHERE t.title = :title")
    fun doesTitleExist(@Param("title") title: String): Boolean
}