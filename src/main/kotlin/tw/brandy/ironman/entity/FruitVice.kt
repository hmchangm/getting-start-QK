package tw.brandy.ironman.entity

data class FruityVice (
    //{ "genus": "Musa", "name": "Banana", "id": 1, "family": "Musaceae", "order": "Zingiberales", "nutritions": { "carbohydrates": 22, "protein": 1, "fat": 0.2, "calories": 96, "sugar": 17.2 } }
    val name : String,
    val genus : String,
    val id : Long,
    val family : String,
    val order : String,
    val nutritions: Nutrition
)

data class Nutrition (
    val carbohydrates: Long,
    val protein: Long,
    val fat: Double,
    val calories: Long,
    val sugar: Double

)