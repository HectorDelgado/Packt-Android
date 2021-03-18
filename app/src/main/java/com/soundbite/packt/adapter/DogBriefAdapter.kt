package com.soundbite.packt.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.soundbite.packt.R
import com.soundbite.packt.databinding.ItemDogBriefBinding
import com.soundbite.packt.model.DogBriefItem

class DogBriefAdapter(private val data: List<DogBriefItem>, private val listener: DogListener) :
    RecyclerView.Adapter<DogBriefAdapter.DogBriefViewHolder>() {

    interface DogListener {
        fun onClick(dog: DogBriefItem)
    }

    class DogBriefViewHolder(
        private val binding: ItemDogBriefBinding,
        private val listener: DogListener
    ) : RecyclerView.ViewHolder(binding.root) {
        // Responsible for binding data to each view that is created
        fun bind(dogBriefItem: DogBriefItem) {
            binding.parentLayout.setOnClickListener { listener.onClick(dogBriefItem) }
            binding.dogIV.setImageResource(R.drawable.ic_temp_dogcircle)
            binding.dogNameTV.text = dogBriefItem.dogName
            binding.dogBreedTV.text = dogBriefItem.dogBreed
        }
        companion object {
            fun from(parent: ViewGroup, viewType: Int, listener: DogListener): DogBriefViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val itemBinding = ItemDogBriefBinding.inflate(layoutInflater, parent, false)
                return DogBriefViewHolder(itemBinding, listener)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DogBriefViewHolder {
        return DogBriefViewHolder.from(parent, viewType, listener)
    }

    override fun onBindViewHolder(holder: DogBriefViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount() = data.size
}
