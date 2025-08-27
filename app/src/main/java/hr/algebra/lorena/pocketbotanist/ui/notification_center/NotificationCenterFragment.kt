package hr.algebra.lorena.pocketbotanist.ui.notification_center

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import hr.algebra.lorena.pocketbotanist.R
import hr.algebra.lorena.pocketbotanist.adapter.NotificationAdapter
import hr.algebra.lorena.pocketbotanist.databinding.FragmentNotificationCenterBinding
import hr.algebra.lorena.pocketbotanist.model.Notification
import hr.algebra.lorena.pocketbotanist.repository.PlantRepository

class NotificationCenterFragment : Fragment() {

    private var _binding: FragmentNotificationCenterBinding? = null
    private val binding get() = _binding!!

    private lateinit var plantRepository: PlantRepository
    private lateinit var notificationAdapter: NotificationAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotificationCenterBinding.inflate(inflater, container, false)
        plantRepository = PlantRepository(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        notificationAdapter = NotificationAdapter(emptyList()) { notification ->
            val bundle = bundleOf("plantId" to notification.plantId)
            findNavController().navigate(R.id.action_nav_notification_center_to_nav_plant_details, bundle)
        }
        binding.rvNotifications.adapter = notificationAdapter

        binding.btnClear.setOnClickListener {
            clearAllNotifications()
        }
    }

    private fun clearAllNotifications() {
        val rowsDeleted = plantRepository.deleteAllNotifications()
        if (rowsDeleted > 0) {
            Toast.makeText(context, getString(R.string.cleared_all_notifications_toast), Toast.LENGTH_SHORT).show()
            loadNotifications()
        } else {
            Toast.makeText(context, getString(R.string.no_notifications_to_clear_toast), Toast.LENGTH_SHORT).show()
        }
    }

    override fun onResume() {
        super.onResume()
        plantRepository.markAllNotificationsAsRead()
        loadNotifications()
    }

    private fun loadNotifications() {
        val notifications = plantRepository.getAllNotifications()
        notificationAdapter.updateData(notifications)
        updateUI(notifications)
    }

    private fun updateUI(notifications: List<Notification>) {
        if (notifications.isEmpty()) {
            binding.tvNoNotifications.visibility = View.VISIBLE
            binding.rvNotifications.visibility = View.GONE
            binding.btnClear.visibility = View.GONE
        } else {
            binding.tvNoNotifications.visibility = View.GONE
            binding.rvNotifications.visibility = View.VISIBLE
            binding.btnClear.visibility = View.VISIBLE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}