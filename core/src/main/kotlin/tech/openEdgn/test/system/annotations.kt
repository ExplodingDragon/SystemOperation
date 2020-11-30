package tech.openEdgn.test.system

import java.lang.reflect.Type


/**
 *
 * @property name String 列名称
 * @property always Boolean 此字段是否必须
 * @constructor
 */

@Retention(AnnotationRetention.RUNTIME)
@kotlin.annotation.Target(AnnotationTarget.FIELD)
annotation class Column(
        val name: String,
        val always: Boolean = true,
        val format: FormatType = FormatType.STRING
)

enum class FormatType(val formatImpl: DataFormat) {
    STRING(StringDataFormat()),
    MINUTES_SECONDS(MinutesAndSecondsDataFormat()),
}

class StringDataFormat : DataFormat {
    override fun format(data: Any): String {
        return data.toString()
    }

}

class MinutesAndSecondsDataFormat : DataFormat {
    override fun format(data: Any): String {
        val l = data as Long
        return String.format("%02d:%02d", l / 60, l % 60)
    }

}

interface DataFormat {
    fun format(data: Any): String
}

@Retention(AnnotationRetention.RUNTIME)
@kotlin.annotation.Target(AnnotationTarget.FIELD)
annotation class Config(
        val name: String
)