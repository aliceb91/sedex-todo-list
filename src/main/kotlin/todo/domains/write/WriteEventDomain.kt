package todo.domains.write

import todo.models.read.TodoEventModel
import todo.models.read.TodoModel
import todo.models.write.EditTodoModel
import todo.models.write.NewTodoModel
import todo.models.write.StatusEditModel
import todo.repositories.EventRepository
import todo.utils.generateTodoFromEvents
import java.time.LocalDateTime

class WriteEventDomain(val repository: EventRepository) {

    private fun generateRandomId(): String {
        val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
        return (1..8).map {
            allowedChars.random()
        }.joinToString("")
    }

    fun addNewTodo(data: NewTodoModel): TodoModel {
        val entityId = generateRandomId()
        val newEvent = TodoEventModel(
            "TODO_ITEM_ADDED",
            LocalDateTime.now().toString(),
            generateRandomId(),
            entityId,
            mapOf(
                "taskName" to data.name,
                "entityId" to entityId,
                "status" to "NOT_DONE"
            )
        )
        val newEventData = repository.writeEventToJson(newEvent) ?: return TodoModel()
        return generateTodoFromEvents(newEventData)
    }

    fun editTodo(data: EditTodoModel): TodoModel {
        val newEventList = repository.writeEventToJson(
            TodoEventModel(
                "TODO_ITEM_NAME_UPDATED",
                LocalDateTime.now().toString(),
                generateRandomId(),
                data.id,
                mapOf(
                    "taskName" to data.name
                )
            )
        ) ?: return TodoModel()
        return generateTodoFromEvents(newEventList)
    }

    fun editTodoStatus(data: StatusEditModel): TodoModel {
        val newEventList = repository.writeEventToJson(
            TodoEventModel(
                "TODO_ITEM_MARKED_AS_DONE",
                LocalDateTime.now().toString(),
                generateRandomId(),
                data.id,
                mapOf(
                    "status" to data.status
                )
            )
        ) ?: return TodoModel()
        return generateTodoFromEvents(newEventList)
    }
}