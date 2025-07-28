package hr.algebra.lorena.pocketbotanist.model

data class Plant(
    val id: Int,
    val name: String,
    val latinName: String,
    val imageUrl: String? = null  // TODO Using String for now, can be a URL
)