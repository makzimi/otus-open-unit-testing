package com.example.unittests.characters.data

import kotlinx.serialization.Serializable

@Serializable
class CharacterEntity (
    val id: String,
    val name: String,
    val image: String,
)
