package todo.domains.read

import todo.repositories.TodoRepository
import todo.models.read.TodoListViewModel
import todo.models.read.TodoViewModel

class ReadDomain(val todoRepository: TodoRepository) {

    fun getAllTodos(status: String): TodoListViewModel {
        val todoList = todoRepository.getAllTodosByStatus(status) ?: return TodoListViewModel()
        val todoListView = todoList.map {
            TodoViewModel(
                it.id,
                it.name
            )
        }
        return TodoListViewModel(todoListView)
    }

    fun getTodoById(id: String): TodoViewModel {
        val todo = todoRepository.getTodoById(id) ?: return TodoViewModel()
        val todoView = TodoViewModel(
            todo.id,
            todo.name
        )
        return todoView
    }
}