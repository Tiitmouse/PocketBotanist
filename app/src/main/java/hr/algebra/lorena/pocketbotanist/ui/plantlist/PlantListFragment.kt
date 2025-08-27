package hr.algebra.lorena.pocketbotanist.ui.plantlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import hr.algebra.lorena.pocketbotanist.MainActivity
import hr.algebra.lorena.pocketbotanist.R
import hr.algebra.lorena.pocketbotanist.adapter.PlantAdapter
import hr.algebra.lorena.pocketbotanist.databinding.FragmentPlantListBinding

class PlantListFragment : Fragment() {

    private var _binding: FragmentPlantListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PlantListViewModel by viewModels()
    private lateinit var plantAdapter: PlantAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlantListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        plantAdapter = PlantAdapter(mutableListOf()) { clickedPlant ->
            val bundle = bundleOf("plantId" to clickedPlant.id)
            findNavController().navigate(R.id.action_nav_plant_list_to_nav_plant_details, bundle)
        }
        binding.rvPlants.adapter = plantAdapter

        viewModel.plants.observe(viewLifecycleOwner) { plants ->
            plantAdapter.updatePlants(plants)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadPlants()
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}