package todo.routes

import org.http4k.core.*
import org.http4k.core.HttpHandler
import org.http4k.routing.routes
import org.http4k.routing.bind
import org.http4k.core.Method.POST
import org.http4k.core.Method.PUT
import org.http4k.core.Method.PATCH
import org.http4k.core.Method.GET
import org.http4k.core.Status.Companion.OK
import org.http4k.format.Jackson.asJsonObject
import org.http4k.routing.path
import org.http4k.format.Jackson.auto
import org.http4k.core.Body
import todo.domains.read.ReadDomain
import todo.domains.write.WriteDomain
import todo.models.write.*
import todo.repositories.JsonTodoRepository

val dataApp: HttpHandler = routes(
    "/todos" bind POST to {req, ->
        //Add an item
        val domain = WriteDomain(JsonTodoRepository())
        val bodyLens = Body.auto<NewTodoModel>().toLens()
        val bodyData = bodyLens(req)
        val newTodoData = domain.addNewTodo(bodyData)
        val responseData = mapOf(
            "id" to newTodoData.id,
            "name" to newTodoData.name,
            "createdDate" to newTodoData.createdDate,
            "modifiedDate" to newTodoData.modifiedDate,
            "status" to newTodoData.status
        ).asJsonObject()
        Response(OK).body(responseData.toString())
            .header("Content-Type", "application/json")
    },
    "todos/{todoId}" bind PUT  to {req, ->
        //Edit an item
        val domain = WriteDomain(JsonTodoRepository())
        val bodyLens = Body.auto<EditExtractionModel>().toLens()
        val bodyData = bodyLens(req)
        val editData = EditTodoModel(
            req.path("todoId") ?: "",
            bodyData.name
        )
        val newTodoData = domain.editTodo(editData)
        val responseData = mapOf(
            "id" to newTodoData.id,
            "name" to newTodoData.name,
            "createdDate" to newTodoData.createdDate,
            "modifiedDate" to newTodoData.modifiedDate,
            "status" to newTodoData.status
        ).asJsonObject()
        Response(OK).body(responseData.toString())
            .header("Content-Type", "application/json")
    },
    "todos/{todoId}" bind PATCH to {req, ->
        //Mark an item as done / not done
        val domain = WriteDomain(JsonTodoRepository())
        val bodyLens = Body.auto<StatusExtractionModel>().toLens()
        val bodyData = bodyLens(req)
        val statusData = StatusEditModel(
            req.path("todoId") ?: "",
            bodyData.status
        )
        val updatedTodoData = domain.editTodoStatus(statusData)
        val responseData = mapOf(
            "id" to updatedTodoData.id,
            "name" to updatedTodoData.name,
            "createdDate" to updatedTodoData.createdDate,
            "modifiedDate" to updatedTodoData.modifiedDate,
            "status" to updatedTodoData.status
        ).asJsonObject()
        Response(OK).body(responseData.toString())
            .header("Content-Type", "application/json")
    },
    "todos/{todoId}" bind GET to { req, ->
        //Get details by id
        val domain = ReadDomain(JsonTodoRepository())
        val id = req.path("todoId") ?: ""
        val todo = domain.getTodoById(id)
        println(todo)
        val responseData = mapOf(
            "id" to todo.id,
            "name" to todo.name,
        ).asJsonObject()
        println(responseData)
        Response(OK).body(responseData.toString())
            .header("Content-Type", "application/json")
    },
    "todos" bind GET to {req, ->
        //Get all specified entries
        val domain = ReadDomain(JsonTodoRepository())
        val status = req.query("status") ?: "ALL"
        val todoList = domain.getAllTodos(status)
        val responseData = todoList.todoListView.map {
            mapOf(
                "id" to it.id,
                "name" to it.name,
            ).asJsonObject()
        }
        Response(OK).body(responseData.toString())
            .header("Content-Type", "application/json")
    }
)