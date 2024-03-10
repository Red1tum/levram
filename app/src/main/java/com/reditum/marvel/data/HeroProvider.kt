package com.reditum.marvel.data

import com.reditum.marvel.ui.theme.PrimaryColors
import kotlinx.coroutines.flow.MutableStateFlow

data class Character(
    val id: Int,
    val name: String,
    val description: String,
    val url: String,
    var colors: PrimaryColors? = null
)

object HeroProvider {
    private val heroesFlow: MutableStateFlow<List<Character>> =
        MutableStateFlow(listOf(
            Character(
                1,
                "A.I.M.",
                "AIM is a terrorist organization bent on destroying the world",
                "http://i.annihil.us/u/prod/marvel/i/mg/6/20/52602f21f29ec.jpg"
            ),
            Character(
                2,
                "Abomination (Emil Blonsky)",
                "Formerly known as Emil Blonsky, a spy of Soviet Yugoslavian origin working for the KGB, the Abomination gained his powers after receiving a dose of gamma radiation similar to that which transformed Bruce Banner into the incredible Hulk.",
                "http://i.annihil.us/u/prod/marvel/i/mg/9/50/4ce18691cbf04.jpg"
            ),
            Character(
                3,
                "Absorbing Man",
                "No description",
                "http://i.annihil.us/u/prod/marvel/i/mg/1/b0/5269678709fb7.jpg"
            ),
            Character(
                4,
                "Alpha Flight",
                "No description",
                "http://i.annihil.us/u/prod/marvel/i/mg/1/60/52695277ee088.jpg"
            ),
            Character(
                5,
                "Ant-Man (Scott Lang)",
                "No description",
                "http://i.annihil.us/u/prod/marvel/i/mg/e/20/52696868356a0.jpg"
            )
        ))

    fun getHeroes(): MutableStateFlow<List<Character>> = heroesFlow

    fun getHero(id: Int): Character = heroesFlow.value.first { it.id == id }
}