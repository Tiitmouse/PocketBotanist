package hr.algebra.lorena.pocketbotanist.ui.plantdetails

import android.app.AlertDialog
import android.content.Intent
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
                Toast.makeText(context, getString(R.string.watering_logged_toast), Toast.LENGTH_SHORT).show()
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
                    Toast.makeText(context, getString(R.string.notifications_enabled_toast), Toast.LENGTH_SHORT).show()
                } else {
                    scheduler.cancelNotifications(it.id)
                    Toast.makeText(context, getString(R.string.notifications_disabled_toast), Toast.LENGTH_SHORT).show()
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
        binding.tvWatering.text = getString(R.string.water_every_days_template, plant.wateringFrequencyDays)
        binding.tvSunlight.text = getString(R.string.prefers_sunlight_template, plant.sunlightPreference)
        binding.swMuteNotifications.isChecked = plant.notificationsEnabled

        updateWateringCountdown(plant)

        Log.d("PlantDetailsFragment", "Binding details for plant: ${plant.name}, Image URL: ${plant.imageUrl}")

        if (!plant.imageUrl.isNullOrBlank()) {
            Picasso.get()
                .load(plant.imageUrl)
                .fit()
                .centerCrop()
                .placeholder(R.drawable.placeholder_plant)
                .error(R.drawable.placeholder_plant)
                .into(binding.ivPlantImageDetail)
        } else {
            binding.ivPlantImageDetail.setImageResource(R.drawable.placeholder_plant)
        }
    }

    private fun updateWateringCountdown(plant: Plant) {
        val nextWateringTime = plant.lastWateredTimestamp + TimeUnit.DAYS.toMillis(plant.wateringFrequencyDays.toLong())
        val timeRemaining = nextWateringTime - System.currentTimeMillis()

        if (timeRemaining <= 0) {
            binding.tvWateringCountdown.text = getString(R.string.next_watering_due)
        } else {
            val days = TimeUnit.MILLISECONDS.toDays(timeRemaining)
            val hours = TimeUnit.MILLISECONDS.toHours(timeRemaining) % 24
            binding.tvWateringCountdown.text = getString(R.string.next_watering_countdown_template, days, hours)
        }
    }

    private fun setupFab(plant: Plant) {
        val mainActivity = activity as? MainActivity
        mainActivity?.showFab()
        mainActivity?.setFabIcon(R.drawable.edit_foreground)
        mainActivity?.setFabClickListener {
            val bundle = bundleOf("plantId" to plant.id, "title" to getString(R.string.edit_plant))
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
                    R.id.action_share -> {
                        sharePlantDetails()
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun sharePlantDetails() {
        currentPlant?.let { plant ->
            val shareText = """
                Check out this plant: ${plant.name} (${plant.latinName})
                
                Description: ${plant.description}
                Care: Water every ${plant.wateringFrequencyDays} days and provide ${plant.sunlightPreference}.
            """.trimIndent()

            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, shareText)
            }
            startActivity(Intent.createChooser(intent, "Share Plant Details"))
        }
    }

    private fun showDeleteConfirmationDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.delete_plant_dialog_title))
            .setMessage(getString(R.string.delete_plant_dialog_message))
            .setPositiveButton(getString(R.string.delete_button)) { _, _ ->
                deletePlant()
            }
            .setNegativeButton(getString(R.string.cancel_button), null)
            .show()
    }

    private fun deletePlant() {
        currentPlant?.let {
            val scheduler = NotificationScheduler(requireContext())
            scheduler.cancelNotifications(it.id)
            plantRepository.deletePlant(it.id)
            Toast.makeText(requireContext(), getString(R.string.plant_deleted_toast), Toast.LENGTH_SHORT).show()
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