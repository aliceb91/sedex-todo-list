package todo.repositories

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import todo.models.read.TodoEventModel
import todo.models.read.TodoModel
import java.io.File

class JsonEventRepository: EventRepository {

    private val mapper = jacksonObjectMapper().registerKotlinModule()

    private fun readFromResources(): String? {
        val json = File("src/main/resources/todo/events.json")
            .inputStream()
            .readBytes()
            .toString(Charsets.UTF_8)
        return json
    }

    private fun writeToResources(newJson: String) {
        File("src/main/resources/todo/events.json")
            .writeText(newJson)
    }

    private fun getJsonData(): MutableList<TodoEventModel>? {
        val data = readFromResources() ?: return null
        return mapper.readValue<MutableList<TodoEventModel>>(data)
    }

    override fun getEventsById(id: String): MutableList<TodoEventModel>? {
        val jsonData: MutableList<TodoEventModel> = getJsonData() ?: return null
        val filteredList = mutableListOf<TodoEventModel>()
        jsonData.filterTo(filteredList) {
            it.entityId == id
        }
        return filteredList
    }

    override fun writeEventToJson(event: TodoEventModel): MutableList<TodoEventModel>? {
        val jsonData = getJsonData() ?: mutableListOf<TodoEventModel>()
        println(jsonData)
        jsonData.add(event)
        println(jsonData)
        val newJsonString = mapper.writeValueAsString(jsonData)
        writeToResources(newJsonString)
        return getEventsById(event.entityId)
    }

    override fun getAllTodos(): MutableList<TodoEventModel>? {
        val jsonData = getJsonData() ?: mutableListOf<TodoEventModel>()
        return jsonData
    }
}