package tech.openEdgn.test.system.process

import tech.openEdgn.test.system.PCB
import tech.openEdgn.test.system.ProcessStatus
import tech.openEdgn.test.system.manager.ISystemManager
import tech.openEdgn.test.system.memory.IMemoryAlgorithm
import kotlin.reflect.KClass

/**
 * 进程调度算法接口
 */
abstract class BaseProcessAlgorithm<T : PCB>(
        protected val memoryAlgorithm: IMemoryAlgorithm,
        protected val manager: ISystemManager
) {

    @Volatile
    var pid: Long = 0
    val createPid: Long
        get() = ++pid

    /**
     * 全部的进程
     */
    @Suppress("UNCHECKED_CAST")
    protected val allProcess: List<T>
        get() = manager.allProcess as List<T>

    /**
     * 未启动的进程
     */
    protected val noCreateProcess: List<T>
        get() = allProcess.filter { it.status == ProcessStatus.CREATE }

    /**
     * 有 PID 的进程
     */
    protected val startedProcess: List<T>
        get() = allProcess.filter { it.pid > 0 }

    /**
     * 运行一个时钟周期
     */
    abstract fun runClockCycle()

    /**
     * 创建一个随机进程并返回
     * @return PCB
     */
    abstract fun addRandomProcess(): T
    fun finishProcess(process: T){
        process.status = ProcessStatus.FINISH
        process.pid = 0
        memoryAlgorithm.finishProcess(process)
    }

    /**
     * cpu 占用率
     */
    abstract val cpuUsage: Double

    /**
     * 展示在 UI 上列表属性
     */
    abstract val displayClass: KClass<T>
}
