package hr.algebra.lorena.pocketbotanist.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import hr.algebra.lorena.pocketbotanist.R
import hr.algebra.lorena.pocketbotanist.databinding.ItemPlantBinding
import hr.algebra.lorena.pocketbotanist.model.Plant

class PlantAdapter(
    private val plants: List<Plant>,
    private val onPlantClick: (Plant) -> Unit
) : RecyclerView.Adapter<PlantAdapter.PlantViewHolder>() {

    inner class PlantViewHolder(private val binding: ItemPlantBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    onPlantClick(plants[adapterPosition])
                }
            }
        }

        fun bind(plant: Plant) {
            binding.tvPlantName.text = plant.name

            // --- THIS IS THE NEW CODE ---
            if (!plant.imageUrl.isNullOrEmpty()) {
                Picasso.get()
                    .load(plant.imageUrl)
                    .placeholder(R.drawable.placeholder_image) // Show placeholder while loading
                    .error(R.drawable.placeholder_image)       // Show placeholder on error
                    .into(binding.ivPlantImage)
            } else {
                // If there's no URL, just show the placeholder
                binding.ivPlantImage.setImageResource(R.drawable.placeholder_image)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlantViewHolder {
        val binding = ItemPlantBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PlantViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlantViewHolder, position: Int) {
        holder.bind(plants[position])
    }

    override fun getItemCount() = plants.size
}