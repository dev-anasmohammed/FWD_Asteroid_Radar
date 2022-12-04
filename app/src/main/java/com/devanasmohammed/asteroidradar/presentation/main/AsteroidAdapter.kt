package com.devanasmohammed.asteroidradar.presentation.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.devanasmohammed.asteroidradar.R
import com.devanasmohammed.asteroidradar.data.model.Asteroid

class AsteroidAdapter : RecyclerView.Adapter<AsteroidAdapter.AsteroidViewHolder>() {

    inner class AsteroidViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameTv: TextView
        val dateTv: TextView
        val statusImage: ImageView

        init {
            nameTv = view.findViewById(R.id.name_tv)
            dateTv = view.findViewById(R.id.date_tv)
            statusImage = view.findViewById(R.id.status_image)
        }
    }

    private val differCallback = object : DiffUtil.ItemCallback<Asteroid>() {
        override fun areItemsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
            return oldItem == newItem
        }

    }

    val differ = AsyncListDiffer(this, differCallback)

    private var onItemClickListener : ((Asteroid)->Unit)? = null

    fun setOnClickListener(listener: (Asteroid)->Unit){
        onItemClickListener =listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AsteroidViewHolder {
        return AsteroidViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_asteroid, parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: AsteroidViewHolder, position: Int) {

        val asteroid = differ.currentList[position]

        holder.apply {
            nameTv.text = asteroid.codename
            dateTv.text = asteroid.closeApproachDate

            if (asteroid.isPotentiallyHazardous) {
                statusImage.setImageResource(R.drawable.ic_status_potentially_hazardous)
            } else {
                statusImage.setImageResource(R.drawable.ic_status_normal)
            }

            //set on item clicked
            itemView.setOnClickListener{
                onItemClickListener?.let {
                    it(differ.currentList[position])
                }
            }
        }
    }

    override fun getItemCount() = differ.currentList.size
}