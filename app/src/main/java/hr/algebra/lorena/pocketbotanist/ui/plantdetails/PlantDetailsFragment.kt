package hr.algebra.lorena.pocketbotanist.ui.plantdetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import hr.algebra.lorena.pocketbotanist.databinding.FragmentPlantDetailsBinding
import hr.algebra.lorena.pocketbotanist.repository.PlantRepository

class PlantDetailsFragment : Fragment() {

    private var _binding: FragmentPlantDetailsBinding? = null
    private val binding get() = _binding!!

    private lateinit var plantRepository: PlantRepository
    private val args: PlantDetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlantDetailsBinding.inflate(inflater, container, false)
        plantRepository = PlantRepository(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val plantId = args.plantId
        val plant = plantRepository.getPlantById(plantId)

        plant?.let {
            binding.tvPlantNameDetail.text = it.name
            binding.tvLatinNameDetail.text = it.latinName
            binding.tvDescription.text = it.description
            binding.tvWatering.text = "Water every ${it.wateringFrequencyDays} days"
            binding.tvSunlight.text = "Prefers ${it.sunlightPreference}"
            // We will load the image in a later step
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
