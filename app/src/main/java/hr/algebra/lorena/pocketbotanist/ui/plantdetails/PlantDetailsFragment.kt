package hr.algebra.lorena.pocketbotanist.ui.plantdetails

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import com.squareup.picasso.Picasso
import hr.algebra.lorena.pocketbotanist.MainActivity
import hr.algebra.lorena.pocketbotanist.R
import hr.algebra.lorena.pocketbotanist.databinding.FragmentPlantDetailsBinding
import hr.algebra.lorena.pocketbotanist.model.Plant
import hr.algebra.lorena.pocketbotanist.repository.PlantRepository
import hr.algebra.lorena.pocketbotanist.utils.NotificationScheduler
import java.util.concurrent.TimeUnit

class PlantDetailsFragment : Fragment() {

    private var _binding: FragmentPlantDetailsBinding? = null
    private val binding get() = _binding!!

    private lateinit var plantRepository: PlantRepository
    private var currentPlant: Plant? = null
    private var plantId: Int = -1

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
        setupMenu()

        plantId = arguments?.getInt("plantId") ?: -1
        loadPlant()
        setupListeners()
    }

    private fun loadPlant() {
        currentPlant = plantRepository.getPlantById(plantId)
        currentPlant?.let {
            bindData(it)
        }
    }

    private fun setupListeners() {
        binding.btnWaterPlant.setOnClickListener {
            currentPlant?.let {
                val now = System.currentTimeMillis()
                plantRepository.updateLastWateredTimestamp(it.id, now)
                val scheduler = NotificationScheduler(requireContext())
                scheduler.scheduleNotifications(it.copy(lastWateredTimestamp = now))
                Toast.makeText(context, "Watering logged!", Toast.LENGTH_SHORT).show()
                loadPlant()
            }
        }

        binding.swMuteNotifications.setOnCheckedChangeListener { _, isChecked ->
            currentPlant?.let {
                val updatedPlant = it.copy(notificationsEnabled = isChecked)
                plantRepository.updatePlant(updatedPlant)
                currentPlant = updatedPlant

                val scheduler = NotificationScheduler(requireContext())
                if (isChecked) {
                    scheduler.scheduleNotifications(updatedPlant)
                    Toast.makeText(context, "Notifications enabled", Toast.LENGTH_SHORT).show()
                } else {
                    scheduler.cancelNotifications(it.id)
                    Toast.makeText(context, "Notifications disabled", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        currentPlant?.let { setupFab(it) }
    }

    private fun bindData(plant: Plant) {
        binding.tvPlantNameDetail.text = plant.name
        binding.tvLatinNameDetail.text = plant.latinName
        binding.tvDescription.text = plant.description
        binding.tvWatering.text = "Water every ${plant.wateringFrequencyDays} days"
        binding.tvSunlight.text = "Prefers ${plant.sunlightPreference}"
        binding.swMuteNotifications.isChecked = plant.notificationsEnabled

        updateWateringCountdown(plant)

        Log.d("PlantDetailsFragment", "Binding details for plant: ${plant.name}, Image URL: ${plant.imageUrl}")

        if (!plant.imageUrl.isNullOrEmpty()) {
            Picasso.get()
                .load(plant.imageUrl)
                .fit()
                .centerCrop()
                .placeholder(R.drawable.placeholder_plant)
                .error(R.drawable.placeholder_plant)
                .into(binding.ivPlantImageDetail)
        } else {
            Log.d("PlantDetailsFragment", "Image URL is empty, setting placeholder directly.")
            binding.ivPlantImageDetail.setImageResource(R.drawable.placeholder_simple_color)
        }
    }

    private fun updateWateringCountdown(plant: Plant) {
        val nextWateringTime = plant.lastWateredTimestamp + TimeUnit.DAYS.toMillis(plant.wateringFrequencyDays.toLong())
        val timeRemaining = nextWateringTime - System.currentTimeMillis()

        if (timeRemaining <= 0) {
            binding.tvWateringCountdown.text = "Watering is due!"
        } else {
            val days = TimeUnit.MILLISECONDS.toDays(timeRemaining)
            val hours = TimeUnit.MILLISECONDS.toHours(timeRemaining) % 24
            binding.tvWateringCountdown.text = String.format("Next watering in %d days and %d hours", days, hours)
        }
    }

    private fun setupFab(plant: Plant) {
        val mainActivity = activity as? MainActivity
        mainActivity?.showFab()
        mainActivity?.setFabIcon(R.drawable.edit_foreground)
        mainActivity?.setFabClickListener {
            val bundle = bundleOf("plantId" to plant.id, "title" to "Edit Plant")
            findNavController().navigate(R.id.action_nav_plant_details_to_nav_edit_plant, bundle)
        }
    }

    private fun setupMenu() {
        (requireActivity() as MenuHost).addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.details_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.action_delete -> {
                        showDeleteConfirmationDialog()
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun showDeleteConfirmationDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Delete Plant")
            .setMessage("Are you sure you want to delete this plant? This action cannot be undone.")
            .setPositiveButton("Delete") { _, _ ->
                deletePlant()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun deletePlant() {
        currentPlant?.let {
            val scheduler = NotificationScheduler(requireContext())
            scheduler.cancelNotifications(it.id)
            plantRepository.deletePlant(it.id)
            Toast.makeText(requireContext(), "Plant deleted", Toast.LENGTH_SHORT).show()
            findNavController().navigateUp()
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