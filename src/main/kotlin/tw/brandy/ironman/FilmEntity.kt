package tw.brandy.ironman

import io.quarkus.mongodb.panache.common.MongoEntity
import org.bson.types.ObjectId
import java.time.LocalDate
import kotlin.properties.Delegates

@MongoEntity(collection = "film")
class FilmEntity {
    var id: ObjectId? = null; // used by MongoDB for the _id field
    lateinit var title: String
    var episodeId by Delegates.notNull<Int>()
    lateinit var director: String
    lateinit var releaseDate: LocalDate
}
