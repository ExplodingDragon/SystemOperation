package tech.openEdgn.test.system.manager

import tech.openEdgn.test.system.memory.IMemoryAlgorithm
import tech.openEdgn.test.system.process.IProcessAlgorithm
import java.io.Closeable

/***
 *
 * 进程管理器
 *
 */
interface ISystemManager :Closeable{
    /**
     * 进程调度算法
     */
    val processAlgorithm: IProcessAlgorithm

    /**
     * 已使用的内存大小
     */
    val usedMemorySize: Long

    /**
     * 全部内存大小
     */
    val allMemorySize: Long



}