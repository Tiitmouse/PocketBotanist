package hr.algebra.lorena.pocketbotanist.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import hr.algebra.lorena.pocketbotanist.databinding.ItemPlantBinding
import hr.algebra.lorena.pocketbotanist.model.Plant

class PlantAdapter(private val plants: List<Plant>) :
    RecyclerView.Adapter<PlantAdapter.PlantViewHolder>() {

    // ViewHolder holds the views for a single item.
    inner class PlantViewHolder(private val binding: ItemPlantBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(plant: Plant) {
            binding.tvPlantName.text = plant.name
            // Here you would typically load the image using a library like Glide or Picasso
            // For now, we'll leave the placeholder image from the layout.
        }
    }

    // Called when RecyclerView needs a new ViewHolder.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlantViewHolder {
        val binding = ItemPlantBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PlantViewHolder(binding)
    }

    // Called to display the data at the specified position.
    override fun onBindViewHolder(holder: PlantViewHolder, position: Int) {
        holder.bind(plants[position])
    }

    // Returns the total number of items in the list.
    override fun getItemCount() = plants.size
}
