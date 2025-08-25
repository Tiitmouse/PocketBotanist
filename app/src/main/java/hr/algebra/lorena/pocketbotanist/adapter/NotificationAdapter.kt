package hr.algebra.lorena.pocketbotanist.adapter

import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import hr.algebra.lorena.pocketbotanist.databinding.ItemNotificationBinding
import hr.algebra.lorena.pocketbotanist.model.Notification

class NotificationAdapter(
    private var notifications: List<Notification>,
    private val onNotificationClick: (Notification) -> Unit
) : RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>() {

    inner class NotificationViewHolder(private val binding: ItemNotificationBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    onNotificationClick(notifications[adapterPosition])
                }
            }
        }

        fun bind(notification: Notification) {
            binding.tvNotificationMessage.text = notification.message
            binding.tvNotificationTimestamp.text = DateUtils.getRelativeTimeSpanString(
                notification.timestamp,
                System.currentTimeMillis(),
                DateUtils.MINUTE_IN_MILLIS
            )
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val binding =
            ItemNotificationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NotificationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        holder.bind(notifications[position])
    }

    override fun getItemCount() = notifications.size

    fun updateData(newNotifications: List<Notification>) {
        notifications = newNotifications
        notifyDataSetChanged()
    }
}