package todo

class Domain(val todoRepository: TodoRepository) {

    fun getAllTodos(status: String): TodoListModel {
        val todoList = todoRepository.getAllTodosByStatus(status) ?: return TodoListModel()
        return TodoListModel(todoList)
    }

    fun getTodoById(id: String): TodoModel {
        val todo = todoRepository.getTodoById(id) ?: return TodoModel()
        return todo
    }

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