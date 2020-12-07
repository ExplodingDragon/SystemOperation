package tech.openEdgn.test.system.pcbs

import tech.openEdgn.test.system.Column
import tech.openEdgn.test.system.FormatType
import tech.openEdgn.test.system.PCB
import tech.openEdgn.test.system.ProcessStatus

/**
 *
 *
 * @constructor
 */
class HRRNPCB(
        @Column("PID", false)
        override var pid: Long = 0,
        @Column("名称", true)
        override var name: String,
        @Column("进程状态", true, format = FormatType.PROCESS_STATUS,sort = true)
        @Volatile
        override var status: ProcessStatus = ProcessStatus.CREATE,
        @Column("启动时间", true, format = FormatType.MINUTES_SECONDS)
        override val startTime: Long,
        @Column("需要时间", true, format = FormatType.MINUTES_SECONDS)
        override val needTime: Long,
        @Volatile
        @Column("响应比", true, format = FormatType.DOUBLE)
        var responseRatio: Double = 0.0,
        @Column("已用CPU时间", false, format = FormatType.MINUTES_SECONDS)
        @Volatile
        override var usedCpuTime: Long = 0,
        @Column("等待时间", false, format = FormatType.MINUTES_SECONDS)
        @Volatile
        var waitTime: Long = 0,
        @Column("内存首地址", false)
        @Volatile
        override var memoryOffset: Int = 0,
        @Column("需要的内存数", false)
        override val needMemory: Int = 0
) : PCB