package tech.openEdgn.test.system.memory

/**
 * 内存调度算法
 */
interface IMemoryAlgorithm {

    /**
     * 内存已使用大小
     */
    val memoryUsageSize: Int

    /**
     * 内存大小
     */
    val memorySize: Int
}
