package todo.domains.write

import todo.repositories.TodoRepository
import todo.models.read.TodoModel
import todo.models.write.EditTodoModel
import todo.models.write.NewTodoModel
import todo.models.write.StatusEditModel

class WriteDomain(val todoRepository: TodoRepository) {

    fun addNewTodo(data: NewTodoModel): TodoModel {
        val newTodo = todoRepository.addNewTodo(data) ?: return TodoModel()
        return newTodo
    }

    fun editTodo(data: EditTodoModel): TodoModel {
        val editedTodo = todoRepository.editTodo(data) ?: return TodoModel()
        return editedTodo
    }

    fun editTodoStatus(data: StatusEditModel): TodoModel {
        val editedStatusTodo = todoRepository.editTodoStatus(data) ?: TodoModel()
        return editedStatusTodo
    }
}