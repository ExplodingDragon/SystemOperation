package tech.openEdgn.test.system.info

import tech.openEdgn.test.system.process.BaseProcessAlgorithm
import java.io.Serializable
import kotlin.reflect.KClass

class ProcessAlgorithmInfo (val name: String, val implClass: KClass<out BaseProcessAlgorithm<*>>) : Serializable {
    override fun toString(): String {
        return name
    }
}