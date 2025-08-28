package hr.algebra.lorena.pocketbotanist.api

import com.google.gson.annotations.SerializedName

data class ApiPlant(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("latin_name") val latinName: String,
    @SerializedName("description") val description: String,
    @SerializedName("watering_frequency") val wateringFrequencyDays: Int,
    @SerializedName("sunlight_preference") val sunlightPreference: String,
    @SerializedName("image_url") val imageUrl: String?
)