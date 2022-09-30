package tw.brandy.ironman.entity

import io.quarkus.runtime.annotations.RegisterForReflection
import java.time.LocalDate

@RegisterForReflection
data class Film(val title: String, val episodeID: Int, val director: String, val releaseDate: LocalDate, val updater: String)
