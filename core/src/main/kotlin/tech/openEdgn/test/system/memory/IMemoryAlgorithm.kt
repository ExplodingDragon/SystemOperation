package tech.openEdgn.test.system.memory

import tech.openEdgn.test.system.PCB


/**
 * 内存调度算法
 */
abstract class IMemoryAlgorithm {
    /**
     * 内存大小
     */
    val memorySize: Int = 4096

    /**
     * 程序结束，
     *
     */
    abstract fun finishProcess(process: PCB)

    /**
     * 尝试为进程分配内存
     * @param process PCB
     * @return Boolean
     */
    abstract fun tryBootProcess(process: PCB): Boolean




    /**
     * 内存已使用大小
     */
    abstract val memoryUsageSize: Int






}
