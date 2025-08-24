package hr.algebra.lorena.pocketbotanist.ui.plantlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import hr.algebra.lorena.pocketbotanist.MainActivity
import hr.algebra.lorena.pocketbotanist.R
import hr.algebra.lorena.pocketbotanist.adapter.PlantAdapter
import hr.algebra.lorena.pocketbotanist.databinding.FragmentPlantListBinding
import hr.algebra.lorena.pocketbotanist.model.Plant
import hr.algebra.lorena.pocketbotanist.repository.PlantRepository

class PlantListFragment : Fragment() {

    private var _binding: FragmentPlantListBinding? = null
    private val binding get() = _binding!!

    private lateinit var plantRepository: PlantRepository
    private lateinit var plantAdapter: PlantAdapter
    private var plants = mutableListOf<Plant>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlantListBinding.inflate(inflater, container, false)
        plantRepository = PlantRepository(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (plantRepository.getAllPlants().isEmpty()) {
            populateDatabase()
        }

        plantAdapter = PlantAdapter(plants) { clickedPlant ->
            val bundle = bundleOf("plantId" to clickedPlant.id)
            findNavController().navigate(R.id.action_nav_plant_list_to_nav_plant_details, bundle)
        }
        binding.rvPlants.adapter = plantAdapter
    }

    override fun onResume() {
        super.onResume()
        loadPlants()
        setupFab()
    }

    private fun setupFab() {
        val mainActivity = activity as? MainActivity
        mainActivity?.showFab()
        mainActivity?.setFabIcon(R.drawable.add_foreground)
        mainActivity?.setFabClickListener {
            findNavController().navigate(R.id.action_nav_plant_list_to_nav_edit_plant)
        }
    }

    override fun onPause() {
        super.onPause()
        (activity as? MainActivity)?.hideFab()
    }

    private fun loadPlants() {
        plants.clear()
        plants.addAll(plantRepository.getAllPlants())
        plantAdapter.notifyDataSetChanged()
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
