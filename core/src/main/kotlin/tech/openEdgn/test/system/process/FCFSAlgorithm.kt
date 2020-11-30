package tech.openEdgn.test.system.process

import tech.openEdgn.test.system.Column
import tech.openEdgn.test.system.PCB
import tech.openEdgn.test.system.ProcessStatus
import tech.openEdgn.test.system.manager.ISystemManager
import tech.openEdgn.test.system.memory.IMemoryAlgorithm

class FCFSAlgorithm(memoryAlgorithm: IMemoryAlgorithm, manager: ISystemManager) :
        BaseProcessAlgorithm<FCFSAlgorithm.FCFSPCB>(memoryAlgorithm, manager) {
    override val cpuUsage: Double  = 0.22

    override fun runClockCycle() {

    }

    class FCFSPCB(
            @Column("PID", false)
            override var pid: Long = 0,
            @Column("名称", true)
            override val name: String,
            @Column("进程状态", true)
            @Volatile
            override var status: ProcessStatus,
            @Column("启动时间", true)
            override val startTime: Long,
            @Column("需要时间", true)
            override val needTime: Long,
            @Column("已用CPU时间", false)
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