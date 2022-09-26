package tw.brandy.ironman

import io.quarkus.test.junit.QuarkusTest
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.fail
import org.junit.jupiter.api.Test
import tw.brandy.ironman.repository.FilmRepository
import javax.inject.Inject

@QuarkusTest
class FilmRepositoryTest {

    @Inject
    lateinit var filmRepository: FilmRepository

    @Test
    fun `test count`() {
        runBlocking {
            filmRepository.count()
                .fold(ifRight = { assertEquals(3, it) }, ifLeft = { fail("Should not here") })
        }
    }
}
