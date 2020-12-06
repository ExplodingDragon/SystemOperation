package tech.openEdgn.test.system.process.impl

import tech.openEdgn.test.system.ProcessStatus
import tech.openEdgn.test.system.manager.ISystemManager
import tech.openEdgn.test.system.memory.IMemoryAlgorithm
import tech.openEdgn.test.system.pcbs.SimplePCB
import tech.openEdgn.test.system.process.CustomAlgorithm
import java.util.*
import kotlin.math.abs

@Suppress("DuplicatedCode")
class FCFSAlgorithm(memoryAlgorithm: IMemoryAlgorithm, manager: ISystemManager) :
        CustomAlgorithm<SimplePCB>(memoryAlgorithm, manager) {
    override val displayClass = SimplePCB::class
    override fun runClockCycle() {
        // 清空就绪队列下的非就绪进程
        clearUnReadyProcess()
        // 将符合条件的进程添加到就绪队列下
        addReadyProcess()
        // 选取运行进程
        if (runProcess.isEmpty() && readyProcess.isNotEmpty()) {
            readyProcess.first.status = ProcessStatus.RUN
        }
        val process = runProcess
        if (process.isNotEmpty()) {
            process[0].usedCpuTime++
        }
        // 移除运行完成的进程
        removeFinishedProcess()
    }


    override fun addRandomProcess() {
        allProcesses.add(SimplePCB(name = "进程 ${System.currentTimeMillis() % 1000}",
                startTime = manager.runTime,
                needTime = abs(abs(Random().nextLong()) % 30 + 3),
                needMemory = abs(abs(Random().nextInt() % 80) + 30)
        ))
    }
}