package tw.brandy.ironman.entity

import io.smallrye.config.ConfigMapping

@ConfigMapping(prefix = "kaq")
interface KaqConfig {

    fun title(): String
    fun id(): Int
    fun name(): String
}
