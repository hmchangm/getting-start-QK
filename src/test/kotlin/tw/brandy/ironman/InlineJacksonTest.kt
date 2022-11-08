package tw.brandy.ironman

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule
import io.quarkus.test.junit.QuarkusTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import javax.inject.Inject


@JvmInline
value class MyInlineClass(val i: Int)

data class MyDataClass(val i: MyInlineClass, @JsonIgnore val _i:Int=0)

data class MyDataClassTwoProps(
    val i: MyInlineClass,
    val j: MyInlineClass
)

data class MyNormalDataClass(val i: Int)

class ObjectMapperInlineClassTest {

    val objectMapper : ObjectMapper = jacksonObjectMapper()
        .registerModule(Jdk8Module()).registerModule(ParameterNamesModule())

    @Test
    fun `data class without inline class`() {
        val obj = MyNormalDataClass(1)

        val json = objectMapper.writeValueAsString(obj)
        Assertions.assertEquals("""{"i":1}""", json)

        val deserialized = objectMapper.readValue(json,MyNormalDataClass::class.java) // works
        Assertions.assertEquals(obj, deserialized)
    }

    @Test
    fun `data class with one inline class`() {
        val obj = MyDataClass(MyInlineClass(1))

        val json = objectMapper.writeValueAsString(obj)
        Assertions.assertEquals("""{"i":1}""", json)

        val deserialized = objectMapper.readValue(json,MyDataClass::class.java)
        Assertions.assertEquals(obj, deserialized)
    }

    @Test
    fun `data class with two inline class`() {
        val obj = MyDataClassTwoProps(MyInlineClass(1), MyInlineClass(2))

        val json = objectMapper.writeValueAsString(obj)
        Assertions.assertEquals("""{"i":1,"j":2}""", json)

        val deserialized = objectMapper.readValue(json,MyDataClassTwoProps::class.java) // throws MismatchedInputException exception
        Assertions.assertEquals(obj, deserialized)
    }

}