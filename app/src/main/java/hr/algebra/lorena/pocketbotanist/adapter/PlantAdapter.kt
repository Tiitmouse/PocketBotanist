package hr.algebra.lorena.pocketbotanist.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import hr.algebra.lorena.pocketbotanist.R
import hr.algebra.lorena.pocketbotanist.databinding.ItemPlantBinding
import hr.algebra.lorena.pocketbotanist.model.Plant

class PlantAdapter(
    private var plants: List<Plant>,
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
            Log.d("PlantAdapter", "Binding plant: ${plant.name}, Image URL: ${plant.imageUrl}")

            if (!plant.imageUrl.isNullOrBlank()) {
                Picasso.get()
                    .load(plant.imageUrl)
                    .fit()
                    .centerCrop()
                    .placeholder(R.drawable.placeholder_plant)
                    .error(R.drawable.placeholder_plant)
                    .into(binding.ivPlantImage)
            } else {
                binding.ivPlantImage.setImageResource(R.drawable.placeholder_plant)
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

    fun updatePlants(newPlants: List<Plant>) {
        plants = newPlants
        notifyDataSetChanged()
    }
}