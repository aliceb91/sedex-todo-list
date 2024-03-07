package todo.models.read

data class TodoEventModel(
    val eventType: String,
    val createdTime: String,
    val eventId: String,
    val entityId: String,
    val eventDetails: Map<String, String>
)
