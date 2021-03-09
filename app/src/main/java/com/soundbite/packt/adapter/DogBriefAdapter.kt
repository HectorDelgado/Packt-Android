package com.soundbite.packt.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.soundbite.packt.databinding.ItemDogBriefBinding
import com.soundbite.packt.model.DogBriefItem

class DogBriefAdapter(private val data: List<DogBriefItem>) :
    RecyclerView.Adapter<DogBriefAdapter.DogBriefViewHolder>() {

    class DogBriefViewHolder(private val binding: ItemDogBriefBinding) : RecyclerView.ViewHolder(binding.root) {
        // Responsible for binding data to each view that is created
        fun bind(dogBriefItem: DogBriefItem) {
            binding.dogNameTV.text = dogBriefItem.dogName
            binding.dogBreedTV.text = dogBriefItem.dogBreed

            // Use Picasso or other library to display image
            //binding.dogIV.setImageBitmap()
        }
        companion object {
            fun from(parent: ViewGroup, viewType: Int) : DogBriefViewHolder {
                val itemBinding = ItemDogBriefBinding.inflate(LayoutInflater.from(parent.context))
                return DogBriefViewHolder(itemBinding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DogBriefViewHolder {
        return DogBriefViewHolder.from(parent, viewType)
    }

    override fun onBindViewHolder(holder: DogBriefViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount() = data.size
}