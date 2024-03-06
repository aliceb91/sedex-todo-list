package todo

import org.http4k.core.*
import org.http4k.core.HttpHandler
import org.http4k.server.SunHttp
import org.http4k.filter.DebuggingFilters.PrintRequest
import org.http4k.server.asServer
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

val app: HttpHandler = routes(
    "/todos" bind POST to {req, ->
        //Add an item
        val domain = Domain(JsonTodoRepository())
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
        val domain = Domain(JsonTodoRepository())
        val bodyLens = Body.auto<EditExtractionModel>().toLens()
        val bodyData = bodyLens(req)
        val editData = EditTodoModel(
            req.path("todoId") ?: "",
            bodyData.name,
            bodyData.status
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
        val domain = Domain(JsonTodoRepository())
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
        val domain = Domain(JsonTodoRepository())
        val id = req.path("todoId") ?: ""
        val todo = domain.getTodoById(id)
        println(todo)
        val responseData = mapOf(
            "id" to todo.id,
            "name" to todo.name,
            "createdDate" to todo.createdDate,
            "modifiedDate" to todo.modifiedDate,
            "status" to todo.status
        ).asJsonObject()
        println(responseData)
        Response(OK).body(responseData.toString())
            .header("Content-Type", "application/json")
    },
    "todos" bind GET to {req, ->
        //Get all specified entries
        val domain = Domain(JsonTodoRepository())
        val status = req.query("status") ?: "ALL"
        val todoList = domain.getAllTodos(status)
        val responseData = todoList.todoList.map {
            mapOf(
                "id" to it.id,
                "name" to it.name,
                "createdDate" to it.createdDate,
                "modifiedDate" to it.modifiedDate,
                "status" to it.status
            ).asJsonObject()
        }
        Response(OK).body(responseData.toString())
            .header("Content-Type", "application/json")
    }
)

fun main() {

    val printingApp: HttpHandler = PrintRequest().then(app)

    val server = printingApp.asServer(SunHttp(9000)).start()

    println("Serverstarted on " + server.port())
}