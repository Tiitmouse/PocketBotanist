package hr.algebra.lorena.pocketbotanist.ui.plantlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import hr.algebra.lorena.pocketbotanist.adapter.PlantAdapter
import hr.algebra.lorena.pocketbotanist.databinding.FragmentPlantListBinding
import hr.algebra.lorena.pocketbotanist.model.Plant

class PlantListFragment : Fragment() {

    private var _binding: FragmentPlantListBinding? = null
    private val binding get() = _binding!!

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

        // Create dummy data for testing
        val dummyPlants = listOf(
            Plant(1, "Monstera Deliciosa", "Monstera deliciosa"),
            Plant(2, "Snake Plant", "Dracaena trifasciata"),
            Plant(3, "Pothos", "Epipremnum aureum"),
            Plant(4, "ZZ Plant", "Zamioculcas zamiifolia"),
            Plant(5, "Fiddle Leaf Fig", "Ficus lyrata"),
            Plant(6, "Spider Plant", "Chlorophytum comosum")
        )

        // Setup the RecyclerView
        val plantAdapter = PlantAdapter(dummyPlants)
        binding.rvPlants.adapter = plantAdapter
        // The layout manager is now set in the XML, but you could
        // also set it here programmatically if you wanted:
        // binding.rvPlants.layoutManager = GridLayoutManager(context, 2)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
