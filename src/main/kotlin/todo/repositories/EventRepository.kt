package todo.repositories

import todo.models.read.TodoEventModel

interface EventRepository {

    fun getEventsById(id: String): MutableList<TodoEventModel>?

    fun writeEventToJson(event: TodoEventModel): MutableList<TodoEventModel>?

    fun getAllTodos(): MutableList<TodoEventModel>?
}