package tech.openEdgn.test.system.memory

import tech.openEdgn.test.system.PCB

/**
 * 内存调度算法
 */
interface IMemoryAlgorithm {
    /**
     * 程序结束，
     *
     */
    fun finishProcess(process: PCB)

    /**
     * 尝试为进程分配内存
     * @param process PCB
     * @return Boolean
     */
    fun tryBootProcess(process: PCB): Boolean

    /**
     * 内存已使用大小
     */
    val memoryUsageSize: Int

    /**
     * 内存大小
     */
    val memorySize: Int
}
