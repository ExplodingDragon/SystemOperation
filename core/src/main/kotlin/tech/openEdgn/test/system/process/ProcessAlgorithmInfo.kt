package tech.openEdgn.test.system.process

import java.io.Serializable
import kotlin.reflect.KClass

class ProcessAlgorithmInfo (val name: String, val implClass: KClass<out IProcessAlgorithm>) : Serializable {
    override fun toString(): String {
        return name
    }
}