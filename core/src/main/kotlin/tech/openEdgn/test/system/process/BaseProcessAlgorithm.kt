package tech.openEdgn.test.system.process

import tech.openEdgn.test.system.PCB
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
    protected val allProcess
        get() = manager.allProcess

    /**
     * 运行一个时钟周期
     */
    abstract fun runClockCycle()

    /**
     * cpu 占用率
     */
    abstract val cpuUsage: Double

    /**
     * 展示在 UI 上列表属性
     */
    abstract val displayClass: KClass<T>
}
