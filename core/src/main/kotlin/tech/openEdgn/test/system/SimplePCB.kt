package tech.openEdgn.test.system

class SimplePCB(
            @Column("PID", false)
            override var pid: Long = 0,
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
            override var usedCpuTime: Long = 0,
            @Column("内存首地址", false)
            @Volatile
            override var memoryOffset: Long = 0,
            @Column("需要的内存数", false)
            override val needMemory: Int = 0
    ) : PCB