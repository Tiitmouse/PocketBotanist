package hr.algebra.lorena.pocketbotanist.ui.editplant

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import hr.algebra.lorena.pocketbotanist.databinding.FragmentEditPlantBinding
import hr.algebra.lorena.pocketbotanist.model.Plant
import hr.algebra.lorena.pocketbotanist.repository.PlantRepository

class EditPlantFragment : Fragment() {

    private var _binding: FragmentEditPlantBinding? = null
    private val binding get() = _binding!!

    private lateinit var plantRepository: PlantRepository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditPlantBinding.inflate(inflater, container, false)
        plantRepository = PlantRepository(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSave.setOnClickListener {
            savePlant()
        }
    }

    private fun savePlant() {
        val name = binding.etPlantName.text.toString().trim()
        val latinName = binding.etLatinName.text.toString().trim()
        val description = binding.etDescription.text.toString().trim()
        val wateringFreqText = binding.etWateringFrequency.text.toString().trim()
        val sunlight = binding.etSunlightPreference.text.toString().trim()
        val imageUrl = binding.etImageUrl.text.toString().trim()

        if (name.isEmpty() || latinName.isEmpty() || description.isEmpty() || wateringFreqText.isEmpty() || sunlight.isEmpty()) {
            Toast.makeText(context, "Please fill out all fields", Toast.LENGTH_SHORT).show()
            return
        }

        val plant = Plant(
            id = 0,
            name = name,
            latinName = latinName,
            description = description,
            wateringFrequencyDays = wateringFreqText.toInt(),
            sunlightPreference = sunlight,
            imageUrl = imageUrl.ifEmpty { null }
        )

        plantRepository.insertPlant(plant)

        Toast.makeText(context, "Plant saved successfully!", Toast.LENGTH_SHORT).show()

        // Navigate back to the plant list
        findNavController().navigateUp()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
