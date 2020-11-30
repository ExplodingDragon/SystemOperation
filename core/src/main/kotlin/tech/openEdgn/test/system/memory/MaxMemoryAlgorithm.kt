package tech.openEdgn.test.system.memory

import tech.openEdgn.test.system.PCB

class MaxMemoryAlgorithm :IMemoryAlgorithm {
    override val memoryUsageSize: Int
        get() = 24
    override val memorySize: Int
        get() = 786

    override fun finishProcess(process: PCB) {

    }
}