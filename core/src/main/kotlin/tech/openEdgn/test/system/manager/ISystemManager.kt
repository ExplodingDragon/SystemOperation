package tech.openEdgn.test.system.manager

import tech.openEdgn.test.system.PCB
import kotlin.reflect.KClass

interface ISystemManager {
    /**
     * 添加随机线程
     */
    fun addRandomProcess()

    /**
     * 发送进程控制指令
     * @param pid Long 进程 PID
     * @param action ProcessAction 指令动作
     */
    fun sendAction(pid:Long,action: ProcessAction)

    /**
     * 阻塞进程数目
     */
    val waitProcessSize: Int

    /**
     * 挂起进程数目
     */
    val hangProcessSize: Int

    /**
     * 内存大小
     */
    val memorySize: Int

    /**
     *  已使用内存
     */
    val memoryUsageSize: Int

    /**
     * 已结束进程数
     */
    val finishProcessSize: Int

    /**
     * 进程总个数
     */
    val processSize: Int

    /**
     * cpu 占用
     *
     */
    val cpuUsage: Double

    /**
     * 运行时间
     */
    val runTime: Long

    /**
     * 全部进程信息
     */
    val allProcess: List<PCB>

    /**
     * 进程配置信息
     */
    val displayClass: KClass<*>
}