package todo

interface TodoRepository {

    fun getAllTodosByStatus(status: String): List<TodoModel>?

    fun getTodoById(id: String): TodoModel?

    fun addNewTodo(data: NewTodoModel): TodoModel?

    fun editTodo(data: EditTodoModel): TodoModel?

    fun editTodoStatus(data: StatusEditModel): TodoModel?
}