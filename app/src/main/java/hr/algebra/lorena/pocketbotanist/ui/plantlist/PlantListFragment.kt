package hr.algebra.lorena.pocketbotanist.ui.plantlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import hr.algebra.lorena.pocketbotanist.adapter.PlantAdapter
import hr.algebra.lorena.pocketbotanist.databinding.FragmentPlantListBinding
import hr.algebra.lorena.pocketbotanist.model.Plant
import hr.algebra.lorena.pocketbotanist.repository.PlantRepository

class PlantListFragment : Fragment() {

    private var _binding: FragmentPlantListBinding? = null
    private val binding get() = _binding!!

    private lateinit var plantRepository: PlantRepository

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

        // Check if the database is empty and populate it if it is.
        if (plantRepository.getAllPlants().isEmpty()) {
            populateDatabase()
        }

        // Load plants from the database and display them.
        val plants = plantRepository.getAllPlants()
        val plantAdapter = PlantAdapter(plants)
        binding.rvPlants.adapter = plantAdapter
    }

    private fun populateDatabase() {
        val dummyPlants = listOf(
            Plant(0, "Monstera Deliciosa", "Monstera deliciosa"),
            Plant(0, "Snake Plant", "Dracaena trifasciata"),
            Plant(0, "Pothos", "Epipremnum aureum"),
            Plant(0, "ZZ Plant", "Zamioculcas zamiifolia"),
            Plant(0, "Fiddle Leaf Fig", "Ficus lyrata"),
            Plant(0, "Spider Plant", "Chlorophytum comosum")
        )
        dummyPlants.forEach { plantRepository.insertPlant(it) }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
