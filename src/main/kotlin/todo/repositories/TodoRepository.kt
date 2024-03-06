package todo.repositories

import todo.models.read.TodoModel
import todo.models.write.EditTodoModel
import todo.models.write.NewTodoModel
import todo.models.write.StatusEditModel

interface TodoRepository {

    fun getAllTodosByStatus(status: String): List<TodoModel>?

    fun getTodoById(id: String): TodoModel?

    fun addNewTodo(data: NewTodoModel): TodoModel?

    fun editTodo(data: EditTodoModel): TodoModel?

    fun editTodoStatus(data: StatusEditModel): TodoModel?
}