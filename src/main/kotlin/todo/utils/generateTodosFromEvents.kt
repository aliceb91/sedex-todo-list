package todo.utils

import todo.models.read.TodoEventModel
import todo.models.read.TodoModel

fun generateTodoFromEvents(data: MutableList<TodoEventModel>): TodoModel {
    var todoData: MutableMap<String, Any> = mutableMapOf<String, Any>(
        "id" to "",
        "name" to "",
        "createdDate" to "",
        "modifiedDate" to "",
        "status" to ""
    )
    data.forEach{
        if (it.eventType == "TODO_ITEM_ADDED") {
            todoData["id"] = it.entityId
            todoData["name"] = it.eventDetails["taskName"] ?: ""
            todoData["createdDate"] = data[0].createdTime
            todoData["modifiedDate"] = it.createdTime
            todoData["status"] = it.eventDetails["status"] ?: ""
        }
        if (it.eventType == "TODO_ITEM_NAME_UPDATED") {
            todoData["name"] = it.eventDetails["taskName"] ?: ""
            todoData["modifiedDate"] = it.createdTime
        }
        if (it.eventType == "TODO_ITEM_MARKED_AS_DONE") {
            todoData["status"] = it.eventDetails["status"] ?: ""
            todoData["modifiedDate"] = it.createdTime
        }
    }
    return TodoModel(
        todoData["id"].toString(),
        todoData["name"].toString(),
        todoData["createdDate"].toString(),
        todoData["modifiedDate"].toString(),
        todoData["status"].toString()
    )
}