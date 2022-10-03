package tw.brandy.ironman.resource

import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.core.CacheControl
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

@Path("/")
class IndexResource {

    companion object {
        private const val INDEX_RESOURCE = "/META-INF/resources/index.html"
    }

    @GET
    @Path("/user/{fileName:.+}")
    suspend fun responseIndexContent(@PathParam("fileName") fileName: String): Response = IndexResource::class
        .java.getResourceAsStream(INDEX_RESOURCE).let {
            Response
                .ok(it)
                .cacheControl(CacheControl.valueOf("max-age=900"))
                .type(MediaType.TEXT_HTML_TYPE)
                .build()
        }

    @GET
    @Path("/task/{fileName:.+}")
    suspend fun responseTaskContent(@PathParam("fileName") fileName: String): Response = responseIndexContent(fileName)
}
