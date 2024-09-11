package com.todo.todo_app.data

import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.time.LocalDateTime


@Entity
@Table(name = "todo", uniqueConstraints = [UniqueConstraint(name = "uk_todo_title", columnNames = ["title"])])
class Todo {

    @Id
    @GeneratedValue(generator = "task_uuid", strategy = GenerationType.UUID)
    @SequenceGenerator(name = "task_uuid", sequenceName = "task_uuid",allocationSize = 1)
    val id: Long = 0

    @Column(name = "createdAt", nullable = false)
    var createdAt: LocalDateTime = LocalDateTime.now()

    @Column(name = "updatedAt", nullable = false)
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