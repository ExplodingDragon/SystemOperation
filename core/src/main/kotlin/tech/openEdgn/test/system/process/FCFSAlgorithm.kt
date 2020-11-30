package tech.openEdgn.test.system.process

import tech.openEdgn.test.system.*
import tech.openEdgn.test.system.manager.ISystemManager
import tech.openEdgn.test.system.memory.IMemoryAlgorithm
import java.util.*
import kotlin.math.abs

class FCFSAlgorithm(memoryAlgorithm: IMemoryAlgorithm, manager: ISystemManager) :
        BaseProcessAlgorithm<SimplePCB>(memoryAlgorithm, manager) {
    override val cpuUsage: Double = 0.22


    override fun runClockCycle() {
        startedProcess.forEach {
            if (it.usedCpuTime >= (it.needTime - 1)) {
                super.finishProcess(it)
            }
        }
    }

    override fun addRandomProcess(): SimplePCB {
        return SimplePCB(name = "进程 ${System.currentTimeMillis() % 1000}",
                startTime = manager.runTime,
                needTime = abs(Random().nextLong() % 30 + 10),
                needMemory = abs(Random().nextInt() % 20 + 7)
        )
    }

    override val displayClass = SimplePCB::class

}