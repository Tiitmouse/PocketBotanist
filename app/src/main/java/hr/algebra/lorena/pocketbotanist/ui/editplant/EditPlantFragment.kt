package hr.algebra.lorena.pocketbotanist.ui.editplant

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import hr.algebra.lorena.pocketbotanist.databinding.FragmentEditPlantBinding
import hr.algebra.lorena.pocketbotanist.model.Plant
import hr.algebra.lorena.pocketbotanist.repository.PlantRepository

class EditPlantFragment : Fragment() {

    private var _binding: FragmentEditPlantBinding? = null
    private val binding get() = _binding!!

    private lateinit var plantRepository: PlantRepository
    private var existingPlant: Plant? = null
    private var plantId: Int = -1

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

        plantId = arguments?.getInt("plantId") ?: -1
        val title = arguments?.getString("title") ?: "Add Plant"
        (activity as AppCompatActivity).supportActionBar?.title = title

        if (plantId != -1) {
            loadPlantData()
        }

        binding.btnSave.setOnClickListener {
            savePlant()
        }
    }

    private fun loadPlantData() {
        existingPlant = plantRepository.getPlantById(plantId)
        existingPlant?.let {
            binding.etPlantName.setText(it.name)
            binding.etLatinName.setText(it.latinName)
            binding.etDescription.setText(it.description)
            binding.etWateringFrequency.setText(it.wateringFrequencyDays.toString())
            binding.etSunlightPreference.setText(it.sunlightPreference)
            binding.etImageUrl.setText(it.imageUrl)
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
            id = plantId.takeIf { it != -1 } ?: 0,
            name = name,
            latinName = latinName,
            description = description,
            wateringFrequencyDays = wateringFreqText.toInt(),
            sunlightPreference = sunlight,
            imageUrl = imageUrl.ifEmpty { null }
        )

        if (existingPlant != null) {
            plantRepository.updatePlant(plant)
            Toast.makeText(context, "Plant updated successfully!", Toast.LENGTH_SHORT).show()
        } else {
            plantRepository.insertPlant(plant)
            Toast.makeText(context, "Plant saved successfully!", Toast.LENGTH_SHORT).show()
        }

        findNavController().navigateUp()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
