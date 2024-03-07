package todo.domains.read

import todo.models.read.TodoEventModel
import todo.models.read.TodoListViewModel
import todo.models.read.TodoModel
import todo.models.read.TodoViewModel
import todo.repositories.EventRepository
import todo.utils.generateTodoFromEvents

class ReadEventDomain(val repository: EventRepository) {

    private fun findAllIds(data: MutableList<TodoEventModel>):MutableList<String> {
        val ids = mutableListOf<String>()
        data.map {
            if (it.entityId !in ids) {
                ids.add(it.entityId)
            }
        }
        return ids
    }

    fun getAllTodosByStatus(status: String): TodoListViewModel {
        val todoList = repository.getAllTodos() ?: return TodoListViewModel()
        val uniqueIds = findAllIds(todoList)
        val assembledEvents = uniqueIds.map{id: String ->
            val eventsById = todoList.filter { event: TodoEventModel ->
                event.entityId == id
            }
            val model = generateTodoFromEvents(eventsById.toMutableList())
            model
        }
        if (status == "ALL") {
            return TodoListViewModel(
                assembledEvents.map {
                    TodoViewModel(
                        it.id,
                        it.name
                    )
                }
            )
        }
        return TodoListViewModel(
            assembledEvents
                .filter {
                    it.status == status
                }
                .map {
                TodoViewModel(
                    it.id,
                    it.name
                )
            }
        )
    }

    fun getTodoById(id: String): TodoViewModel {
        val events = repository.getEventsById(id) ?: return TodoViewModel()
        val todoModel = generateTodoFromEvents(events)
        return TodoViewModel(
            todoModel.id,
            todoModel.name
        )
    }
}