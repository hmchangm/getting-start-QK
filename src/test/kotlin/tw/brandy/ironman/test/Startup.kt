package tw.brandy.ironman.test

import io.quarkus.runtime.StartupEvent
import io.smallrye.mutiny.coroutines.awaitSuspending
import io.vertx.mutiny.pgclient.PgPool
import kotlinx.coroutines.runBlocking
import tw.brandy.ironman.repository.FilmRepository
import javax.enterprise.context.ApplicationScoped
import javax.enterprise.event.Observes

@ApplicationScoped
class Startup(val client: PgPool, val filmRepository: FilmRepository) {

    fun onStart(@Observes ev: StartupEvent?) {
        runBlocking {
            client.query("DROP TABLE IF EXISTS films").execute().awaitSuspending()
            client.query(
                """CREATE TABLE films
                    (episodeId integer PRIMARY KEY, title TEXT NOT NULL,director TEXT NOT NULL, 
                    releaseDate DATE, updater TEXT NOT NULL)"""
            ).execute().awaitSuspending()
            client.query(
                "INSERT INTO films (episodeId,title,director,releaseDate,updater) " +
                    "VALUES (4,'A New Hope','George Lucas','1977-05-25','brandy')"
            ).execute().awaitSuspending()
            client.query(
                "INSERT INTO films (episodeId,title,director,releaseDate,updater) " +
                    "VALUES (5,'The Empire Strikes Back','George Lucas','1980-05-21','brandy')"
            ).execute().awaitSuspending()
            client.query(
                "INSERT INTO films (episodeId,title,director,releaseDate,updater) " +
                    "VALUES (6,'Return Of The Jedi','George Lucas','1983-05-21','brandy')"
            ).execute().awaitSuspending()

            println("Data inserts ${filmRepository.findAll()}")
        }
    }
}
