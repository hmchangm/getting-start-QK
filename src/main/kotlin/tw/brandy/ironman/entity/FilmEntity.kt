package tw.brandy.ironman.entity

import io.quarkus.mongodb.panache.kotlin.PanacheMongoEntityBase
import org.bson.codecs.pojo.annotations.BsonCreator
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.codecs.pojo.annotations.BsonProperty
import org.bson.types.ObjectId
import java.time.LocalDate

data class FilmEntity @BsonCreator constructor(
    @BsonId
    var id: ObjectId? = null,
    @BsonProperty("title") var title: String,
    @BsonProperty("episodeId") var episodeId: Int,
    @BsonProperty("director") var director: String,
    @BsonProperty("releaseDate") var releaseDate: LocalDate
) : PanacheMongoEntityBase()
