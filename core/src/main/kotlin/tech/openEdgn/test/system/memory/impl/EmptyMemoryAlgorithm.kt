package tech.openEdgn.test.system.memory.impl

import com.github.openEdgn.logger4k.getLogger
import tech.openEdgn.test.system.PCB
import tech.openEdgn.test.system.memory.IMemoryAlgorithm

class EmptyMemoryAlgorithm : IMemoryAlgorithm {
    private val logger = getLogger()

    @Volatile
    override var memoryUsageSize: Int = 0
    override val memorySize: Int = 100



    @Synchronized
    override fun finishProcess(process: PCB) {

    }

    @Synchronized
    override fun tryBootProcess(process: PCB): Boolean {
        return true
    }
}