package tech.openEdgn.test.system.info

import tech.openEdgn.test.system.memory.IMemoryAlgorithm
import java.io.Serializable
import kotlin.reflect.KClass

class MemoryAlgorithmInfo(val name: String, val implClass: KClass<out IMemoryAlgorithm>) :Serializable{
    override fun toString(): String {
        return name
    }
}