package tech.openEdgn.test.system.process.impl

import tech.openEdgn.test.system.manager.ISystemManager
import tech.openEdgn.test.system.memory.IMemoryAlgorithm
import tech.openEdgn.test.system.pcbs.HRRNPCB
import tech.openEdgn.test.system.process.CustomAlgorithm

class HRRNAlgorithm(memoryAlgorithm: IMemoryAlgorithm, manager: ISystemManager) :
        CustomAlgorithm<HRRNPCB>(memoryAlgorithm, manager) {
    override fun runClockCycle() {

    }

    override fun addRandomProcess() {
    }

    override val displayClass = HRRNPCB::class
}