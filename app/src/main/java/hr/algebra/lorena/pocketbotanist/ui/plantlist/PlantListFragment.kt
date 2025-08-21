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
            Plant(0, "Monstera Deliciosa", "Monstera deliciosa", "A popular houseplant with iconic, split leaves.", 7, "Bright, Indirect Light", null),
            Plant(0, "Snake Plant", "Dracaena trifasciata", "Extremely hardy and known for its air-purifying qualities.", 14, "Low to Bright Indirect Light", null)
        )
        dummyPlants.forEach { plantRepository.insertPlant(it) }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
