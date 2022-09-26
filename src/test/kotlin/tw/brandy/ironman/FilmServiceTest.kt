package tw.brandy.ironman

import arrow.core.left
import io.quarkus.test.junit.QuarkusTest
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import tw.brandy.ironman.service.FilmService
import javax.inject.Inject

@QuarkusTest
class FilmServiceTest {

    @Inject
    lateinit var filmService: FilmService

    @Test
    fun `test delete no this item`() {
        runBlocking {
            assertEquals(AppError.NoThisFilm(1000).left(), filmService.delete(1000))
        }
    }
}
