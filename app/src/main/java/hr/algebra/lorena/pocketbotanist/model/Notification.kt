package hr.algebra.lorena.pocketbotanist.model

data class Notification(
    val id: Int,
    val plantId: Int,
    val message: String,
    val timestamp: Long,
    val isRead: Boolean
)