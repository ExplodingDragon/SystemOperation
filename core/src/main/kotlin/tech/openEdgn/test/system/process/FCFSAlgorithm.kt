package tech.openEdgn.test.system.process

import tech.openEdgn.test.system.Column
import tech.openEdgn.test.system.FormatType
import tech.openEdgn.test.system.PCB
import tech.openEdgn.test.system.ProcessStatus
import tech.openEdgn.test.system.manager.ISystemManager
import tech.openEdgn.test.system.memory.IMemoryAlgorithm
import java.util.*
import kotlin.math.abs

class FCFSAlgorithm(memoryAlgorithm: IMemoryAlgorithm, manager: ISystemManager) :
        BaseProcessAlgorithm<FCFSAlgorithm.FCFSPCB>(memoryAlgorithm, manager) {
    override val cpuUsage: Double = 0.22


    override fun runClockCycle() {
        startedProcess.forEach {
            if (it.usedCpuTime >= (it.needTime - 1)) {
                super.finishProcess(it)
            }
        }
    }

    override fun addRandomProcess(): FCFSPCB {
        return FCFSPCB(name = "进程 ${System.currentTimeMillis() % 1000}",
                startTime = manager.runTime,
                needTime = abs(Random().nextLong() % 30 + 10),
                needMemory = abs(Random().nextInt() % 20 + 7)
        )
    }

    class FCFSPCB(
            @Column("PID", false)
            override var pid: Long = 0,
            @Volatile
            @Column("名称", true)
            override var name: String,
            @Column("进程状态", true)
            @Volatile
            override var status: ProcessStatus = ProcessStatus.CREATE,
            @Column("启动时间", true, format = FormatType.MINUTES_SECONDS)
            override val startTime: Long,
            @Column("需要时间", true, format = FormatType.MINUTES_SECONDS)
            override val needTime: Long,
            @Column("已用CPU时间", false, format = FormatType.MINUTES_SECONDS)
            @Volatile
            var usedCpuTime: Long = 0,
            @Column("内存首地址", false)
            @Volatile
            var memoryOffset: Long = 0,
            @Column("需要的内存数", false)
            override val needMemory: Int = 0
    ) : PCB

    override val displayClass = FCFSPCB::class

}