package tw.brandy.ironman

import io.quarkus.test.junit.QuarkusTest
import io.smallrye.mutiny.coroutines.awaitSuspending
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import javax.inject.Inject

@QuarkusTest
class FilmRepositoryTest {

    @Inject
    lateinit var filmRepository: FilmRepository

    @Test
    fun `test count`() {
        runBlocking {
            filmRepository.count().awaitSuspending()
                .let { assertEquals(3, it) }
        }
    }
}
