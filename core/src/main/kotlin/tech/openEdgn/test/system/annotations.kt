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
        val format: String = "%s"
)

@Retention(AnnotationRetention.RUNTIME)
@kotlin.annotation.Target(AnnotationTarget.FIELD)
annotation class Config(
        val name: String
)