package tech.openEdgn.test.system

import java.lang.annotation.Inherited
import java.lang.reflect.Type


/**
 *
 * @property name String 列名称
 * @property always Boolean 此字段是否必须
 * @constructor
 */
@kotlin.annotation.Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class Column(
        val name: String,
        val always: Boolean = true,
        val format: FormatType = FormatType.STRING
)

enum class FormatType(val formatImpl: DataFormat<out Any>) {
    STRING(StringDataFormat()),
    MINUTES_SECONDS(MinutesAndSecondsDataFormat()),
    PROCESS_STATUS(ProcessStatusDataFormat()),;


}

class ProcessStatusDataFormat: DataFormat<ProcessStatus>() {
    override fun format(data: ProcessStatus): String {
        return data.type
    }

}

class StringDataFormat : DataFormat<Any>() {
    override fun format(data: Any): String {
        return data.toString()
    }

}

class MinutesAndSecondsDataFormat : DataFormat<Any>() {
    override fun format(data: Any): String {
        val l = data as Long
        return String.format("%02d:%02d", l / 60, l % 60)
    }

}

@Suppress("UNCHECKED_CAST")
abstract class DataFormat<T:Any> {
    fun formatStr(data: Any):String{
        return format(data as T)
    }
    abstract fun format(data: T): String
}

@Retention(AnnotationRetention.RUNTIME)
@kotlin.annotation.Target(AnnotationTarget.FIELD)
annotation class Config(
        val name: String
)