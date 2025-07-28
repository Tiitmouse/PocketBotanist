package hr.algebra.lorena.pocketbotanist.model

data class Plant(
    val id: Int,
    val name: String,
    val latinName: String,
    val description: String,
    val wateringFrequencyDays: Int,
    val sunlightPreference: String,
    val imageUrl: String? = null
)