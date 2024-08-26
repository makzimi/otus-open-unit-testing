package com.example.unittests.characters.presentation.adapter

import android.content.res.ColorStateList
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.unittests.R
import com.example.unittests.characters.presentation.CharacterState
import com.example.unittests.common.bump
import com.example.unittests.databinding.ItemCharacterBinding

class CharacterHolder(
    private val binding: ItemCharacterBinding,
    private val onAddToFavorites: (String) -> Unit,
    private val onRemoveFromFavorites: (String) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(characterState: CharacterState) {
        binding.image.load(characterState.image)
        binding.name.text = characterState.name

        bindFavorite(characterState)
    }

    fun bindFavorite(characterState: CharacterState) {
        if (characterState.isFavorite) {
            binding.favorite.setImageResource(R.drawable.ic_favorite_on)
            binding.favorite.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(binding.root.context, R.color.red))
            binding.favorite.setOnClickListener {
                onRemoveFromFavorites(characterState.id)
                binding.favorite.bump()
            }
        } else {
            binding.favorite.setImageResource(R.drawable.ic_favorite_off)
            binding.favorite.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(binding.root.context, R.color.grey))
            binding.favorite.setOnClickListener {
                onAddToFavorites(characterState.id)
                binding.favorite.bump()
            }
        }
    }
}
