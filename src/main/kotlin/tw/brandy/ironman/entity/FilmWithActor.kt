package tw.brandy.ironman.entity

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.annotation.JsonDeserialize

@JsonDeserialize(using = ActorDeserializer::class)
@JvmInline
value class Actor(val raw: String)

class ActorDeserializer : JsonDeserializer<Actor>() {
    override fun deserialize(p: JsonParser, ctxt: DeserializationContext): Actor {
        val str: String = p.readValueAs(String::class.java)
        return Actor(str)
    }
}

data class FilmWithActor(
    val episodeId: EpisodeId,
    val title: Title,
    val director: Director,
    val actors: List<Actor>
)
