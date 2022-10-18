package tw.brandy.ironman.entity

import kotlinx.serialization.Serializable

@Serializable
data class FruityVice(
    val name: String,
    val genus: String,
    val id: Long,
    val family: String,
    val order: String,
    val nutritions: Nutrition
)

@Serializable
data class Nutrition(val carbohydrates: Long, val protein: Long, val fat: Double, val calories: Long, val sugar: Double)
