package hr.algebra.lorena.pocketbotanist.ui.plantlist

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import hr.algebra.lorena.pocketbotanist.model.Plant
import hr.algebra.lorena.pocketbotanist.repository.PlantRepository

class PlantListViewModel(application: Application) : AndroidViewModel(application) {

    private val plantRepository: PlantRepository = PlantRepository(application)

    private val _plants = MutableLiveData<List<Plant>>()
    val plants: LiveData<List<Plant>> = _plants

    fun loadPlants() {
        if (plantRepository.getAllPlantsFromProvider().isEmpty()) {
            populateDatabase()
        }
        _plants.value = plantRepository.getAllPlantsFromProvider()
    }

    private fun populateDatabase() {
        val dummyPlants = listOf(
            Plant(0, "Monstera Deliciosa", "Monstera deliciosa", "A popular houseplant with iconic, split leaves.", 7, "Bright, Indirect Light", "https://images.pexels.com/photos/6597437/pexels-photo-6597437.jpeg"),
            Plant(0, "Snake Plant", "Dracaena trifasciata", "Extremely hardy and known for its air-purifying qualities.", 14, "Low to Bright Indirect Light", "https://images.pexels.com/photos/2123482/pexels-photo-2123482.jpeg"),
            Plant(0, "ZZ Plant", "Zamioculcas zamiifolia", "A low-maintenance plant with wide, dark green leaves, tolerant of low light and drought.", 21, "Low to Bright Indirect Light", "https://upload.wikimedia.org/wikipedia/commons/thumb/c/cf/Zamioculcas_zamiifolia_1.jpg/500px-Zamioculcas_zamiifolia_1.jpg"),
            Plant(0, "Fiddle Leaf Fig", "Ficus lyrata", "A stunning statement plant with large, violin-shaped leaves that requires consistent care.", 10, "Bright, Indirect Light", "https://images.pexels.com/photos/8989424/pexels-photo-8989424.jpeg"),
            Plant(0, "Golden Pothos", "Epipremnum aureum", "A popular, easy-to-care-for vining plant with heart-shaped leaves variegated with yellow.", 9, "Low to Bright Indirect Light", "https://images.pexels.com/photos/5865414/pexels-photo-5865414.jpeg"),
            Plant(0, "Spider Plant", "Chlorophytum comosum", "Known for its arching leaves and 'spiderettes' that dangle down like spiders on a web.", 7, "Bright, Indirect Light", "https://images.pexels.com/photos/2308403/pexels-photo-2308403.jpeg"),
            Plant(0, "Peace Lily", "Spathiphyllum", "Famous for its elegant white spathes and dark green leaves. It visibly droops when it needs water.", 7, "Medium to Low Indirect Light", "https://images.pexels.com/photos/13256146/pexels-photo-13256146.jpeg"),
            Plant(0, "Rubber Plant", "Ficus elastica", "Features large, glossy, dark green leaves. A relatively easy-going statement plant.", 12, "Bright, Indirect Light", "https://images.pexels.com/photos/213727/pexels-photo-213727.jpeg"),
            Plant(0, "Aloe Vera", "Aloe barbadensis miller", "A succulent known for its medicinal gel-filled leaves. Prefers to dry out completely between waterings.", 21, "Bright, Direct Light", "https://images.pexels.com/photos/1671650/pexels-photo-1671650.jpeg"),
            Plant(0, "String of Pearls", "Senecio rowleyanus", "A unique succulent with trailing stems of pearl-like leaves. Requires careful watering.", 14, "Bright, Indirect Light", "https://images.pexels.com/photos/10828063/pexels-photo-10828063.jpeg"),
            Plant(0, "Calathea Orbifolia", "Calathea orbifolia", "A beautiful prayer plant with large, round leaves striped with silver. Enjoys high humidity.", 7, "Medium to Bright Indirect Light", "https://images.pexels.com/photos/13798092/pexels-photo-13798092.jpeg"),
            Plant(0, "Bird of Paradise", "Strelitzia nicolai", "A large, tropical plant with banana-like leaves that brings a jungle vibe indoors.", 8, "Bright, Direct Light", "https://images.pexels.com/photos/16024122/pexels-photo-16024122.jpeg")
        )
        dummyPlants.forEach { plantRepository.insertPlant(it) }
    }
}