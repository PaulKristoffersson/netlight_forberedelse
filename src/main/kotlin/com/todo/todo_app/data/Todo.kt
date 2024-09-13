package com.todo.todo_app.data

import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.time.LocalDateTime
import java.util.*


@Entity
@Table(name = "todo", uniqueConstraints = [UniqueConstraint(name = "uk_todo_title", columnNames = ["title"])])
class Todo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: UUID? = null

    @Column(name = "created_at", nullable = false)
    val createdAt: LocalDateTime = LocalDateTime.now()

    @Column(name = "updated_at", nullable = false)
    var updatedAt: LocalDateTime = LocalDateTime.now()

    @NotBlank
    @Column(name = "title", nullable = false, unique = true)
    var title: String = ""

    @Column(name = "description", nullable = false)
    var description: String = ""


    @Column(name = "rank", nullable = false)
    var rank: Int = 0

    @Column(name = "done", nullable = false)
    var done: Boolean = false

}