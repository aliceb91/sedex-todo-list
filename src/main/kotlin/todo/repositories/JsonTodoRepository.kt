package todo.repositories

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import todo.models.read.TodoModel
import todo.models.write.EditTodoModel
import todo.models.write.NewTodoModel
import todo.models.write.StatusEditModel
import java.time.LocalDateTime
import java.io.File

class JsonTodoRepository: TodoRepository {

    private val mapper = jacksonObjectMapper().registerKotlinModule()


    private fun readFromResources(): String? {
        val json = File("src/main/resources/todo/todos.json")
            .inputStream()
            .readBytes()
            .toString(Charsets.UTF_8)
        return json
    }
//    private fun readFromResources(): String? {
//        val json = this::class.java.getResourceAsStream("todos.json")
//            ?.bufferedReader()
//            ?.readText()
//        return json
//    }

    private fun writeToResources(newJson: String) {
        File("src/main/resources/todo/todos.json")
            .writeText(newJson)
    }

    private fun getJsonData(): MutableList<TodoModel>? {
        val data = readFromResources() ?: return null
        return mapper.readValue<MutableList<TodoModel>>(data)
    }

    private fun generateRandomId(): String {
        val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
        return (1..8).map {
            allowedChars.random()
        }.joinToString("")
    }

    override fun getAllTodosByStatus(status: String): List<TodoModel>? {
        val jsonData: MutableList<TodoModel> = getJsonData() ?: return null
        if (status == "ALL") {
            return jsonData
        }
        val filteredData = jsonData.filter {
            it.status == status
        }
        return filteredData
    }

    override fun getTodoById(id: String): TodoModel? {
        val jsonData: List<TodoModel> = getJsonData() ?: return null
        val targetTodo = jsonData.filter{
            it.id == id
        }
        if (targetTodo.isEmpty()) {
            return null
        }
        return targetTodo.first()
    }

    override fun addNewTodo(data: NewTodoModel): TodoModel? {
        val newId = generateRandomId()
        val newTodo = TodoModel(
            newId,
            data.name,
            LocalDateTime.now().toString(),
            LocalDateTime.now().toString(),
            "NOT_DONE"
        )
        val jsonData = getJsonData() ?: return null
        jsonData.add(newTodo)
        val newData = mapper.writeValueAsString(jsonData)
        writeToResources(newData)
        return getTodoById(newId)
    }

    override fun editTodo(data: EditTodoModel): TodoModel? {
        val targetId = data.id
        val jsonData = getJsonData() ?: return null
        val targetTodo = jsonData.filter{
            it.id == targetId
        }.first()
        val newTodo = TodoModel(
            targetTodo.id,
            data.name,
            targetTodo.createdDate,
            LocalDateTime.now().toString(),
            targetTodo.status
        )
        val newJsonList = jsonData.map {
            if (it.id == targetId) newTodo else it
        }
        val newData = mapper.writeValueAsString(newJsonList)
        writeToResources(newData)
        return getTodoById(targetId)
    }

    override fun editTodoStatus(data: StatusEditModel): TodoModel? {
        val targetId = data.id
        val jsonData = getJsonData() ?: return null
        val targetTodo = jsonData.filter{
            it.id == targetId
        }.first()
        val newTodo = TodoModel(
            targetTodo.id,
            targetTodo.name,
            targetTodo.createdDate,
            LocalDateTime.now().toString(),
            data.status
        )
        val newJsonList = jsonData.map {
            if (it.id == targetId) newTodo else it
        }
        val newData = mapper.writeValueAsString(newJsonList)
        writeToResources(newData)
        return getTodoById(targetId)
    }
}