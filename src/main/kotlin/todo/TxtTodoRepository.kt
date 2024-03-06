package todo

class TxtTodoRepository: TodoRepository {

    private fun readFromResources(): List<String>? {
        return this::class.java.getResourceAsStream("todos.txt")
            ?.bufferedReader()
            ?.readLines()
    }

    override fun getAllTodosByStatus(status: String): List<TodoModel>? {
        val todoList = readFromResources() ?: return null
        val todoListSplit = todoList.map {
            it.split(",")
        }
        val todoListModel = todoListSplit.map {
            TodoModel(
                it[0],
                it[1],
                it[2],
                it[3],
                it[4]
            )
        }
        return todoListModel
    }

    override fun getTodoById(id: String): TodoModel? {
        TODO()
    }

    override fun addNewTodo(data: NewTodoModel): TodoModel? {
        TODO()
    }

    override fun editTodo(data: EditTodoModel): TodoModel? {
        TODO()
    }

    override fun editTodoStatus(data: StatusEditModel): TodoModel? {
        TODO()
    }
}