package tw.brandy.ironman

import arrow.core.left
import io.quarkus.test.junit.QuarkusTest
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import tw.brandy.ironman.entity.EpisodeId
import tw.brandy.ironman.service.FilmService
import java.util.*
import javax.inject.Inject

@QuarkusTest
class FilmServiceTest {

    @Inject
    lateinit var filmService: FilmService

    @Test
    fun `test delete no this item`() {
        runBlocking {
            val id = EpisodeId(UUID.randomUUID())
            assertEquals(NoThisFilm(id).left(), filmService.delete(id))
        }
    }
}
