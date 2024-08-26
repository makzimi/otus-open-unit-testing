package com.example.unittests.characters.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.unittests.characters.presentation.CharacterState
import com.example.unittests.databinding.ItemCharacterBinding

class CharactersAdapter(
    private val onAddToFavorites: (String) -> Unit,
    private val onRemoveFromFavorites: (String) -> Unit,
) :
    ListAdapter<CharacterState, CharacterHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterHolder {
        return CharacterHolder(
            binding = ItemCharacterBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            ),
            onAddToFavorites = onAddToFavorites,
            onRemoveFromFavorites = onRemoveFromFavorites
        )
    }

    override fun onBindViewHolder(holder: CharacterHolder, position: Int) {
        val entity = getItem(position)
        entity?.let {
            holder.bind(entity)
        }
    }

    override fun onBindViewHolder(
        holder: CharacterHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads)
        } else {
            if (payloads[0] == true) {
                holder.bindFavorite(getItem(position))
            }
        }
    }
}

private class DiffCallback : DiffUtil.ItemCallback<CharacterState>() {

    override fun areItemsTheSame(oldItem: CharacterState, newItem: CharacterState): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: CharacterState, newItem: CharacterState): Boolean {
        return oldItem == newItem
    }

    override fun getChangePayload(oldItem: CharacterState, newItem: CharacterState): Any? {
        if (oldItem.isFavorite != newItem.isFavorite) {
            return true
        }

        return null
    }
}
