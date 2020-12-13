package tech.openEdgn.test.system.process

import com.github.openEdgn.logger4k.getLogger
import tech.openEdgn.test.system.PCB
import tech.openEdgn.test.system.ProcessStatus
import tech.openEdgn.test.system.manager.ISystemManager
import tech.openEdgn.test.system.manager.ProcessAction
import tech.openEdgn.test.system.memory.IMemoryAlgorithm
import java.util.*


/**
 * 进程调度算法接口
 */
abstract class SimpleProcessAlgorithm<T : PCB>(
        memoryAlgorithm: IMemoryAlgorithm,
        manager: ISystemManager
) : BaseProcessAlgorithm<T>(memoryAlgorithm, manager) {


    override val allProcesses: LinkedList<T> = LinkedList()

    open override fun close() {
        synchronized(allProcesses) {
            allProcesses.clear()
        }
    }

    fun isFinished(process: T):Boolean  = process.needTime - 1 <= process.usedCpuTime


    /**
     * 正在运行的进程
     */
    override val runProcess: List<T>
        get() =
            allProcesses.filter { it.status == ProcessStatus.RUN }


    protected val logger = getLogger()

    @Volatile
    var pid: Long = 0

    private val createPid: Long
        get() = ++pid

    /**
     * 已启动的进程
     */
    override val startedProcess: List<T>
        get() = allProcesses.filter { it.pid != 0L }

    /**
     * 未启动进程
     */
    override val noCreateProcess: List<T>
        get() = allProcesses.filter { it.status == ProcessStatus.CREATE }


    /**
     * 将进程销毁
     *
     * @param process T
     */
    fun finishProcess(process: T) {
        synchronized(process) {
            memoryAlgorithm.finishProcess(process)
            process.status = ProcessStatus.FINISH
            process.pid = 0
        }
    }

    fun tryBootProcess(process: T): Boolean {
        synchronized(process) {
            process.pid = pid + 1
            return if (memoryAlgorithm.tryBootProcess(process)) {
                process.pid = createPid
                process.status = ProcessStatus.RUN_READY
                logger.info("进程 {} 已就绪.", process.name)
                true
            } else {
                process.pid = 0
                false
            }
        }
    }


    override fun sendAction(pid: Long, action: ProcessAction) {
        val process = getPcbByPid(pid)
        when (action) {
            ProcessAction.WAIT -> {
                updateStatusByWaitAction(process)
            }
            ProcessAction.STOP -> {
                updateStatusByStopAction(process)
            }
        }
    }

    /**
     * 处理阻塞方法
     * @param process T
     */
    private fun updateStatusByWaitAction(process: T) {

        synchronized(process) {
            val oldStatus = process.status
            process.status = when (process.status) {
                ProcessStatus.RUN -> ProcessStatus.RUN_WAIT
                ProcessStatus.RUN_WAIT -> ProcessStatus.RUN_READY
                ProcessStatus.STOP_WAIT -> ProcessStatus.STOP_READY
                else -> process.status
            }
            logger.info("进程[{}]激活阻塞操作，由{}变为{}.", process.pid, oldStatus.type, process.status.type)
            if (process.status != oldStatus) {
                processStatusHook(process, oldStatus, process.status)
            }
        }
    }

    /**
     * 处理挂起方法
     * @param process T
     */
    private fun updateStatusByStopAction(process: T) {
        synchronized(process) {
            val oldStatus = process.status
            process.status = when (process.status) {
                ProcessStatus.CREATE -> {
                    if (tryBootProcess(process)) {
                        ProcessStatus.STOP_READY
                    } else {
                        process.status
                    }
                }
                ProcessStatus.RUN,
                ProcessStatus.RUN_READY
                -> ProcessStatus.STOP_READY
                ProcessStatus.STOP_WAIT -> ProcessStatus.RUN_WAIT
                ProcessStatus.STOP_READY -> ProcessStatus.RUN_READY
                ProcessStatus.RUN_WAIT -> ProcessStatus.STOP_WAIT
                else -> process.status
            }
            logger.info("进程[{}]激活挂起操作，由{}变为{}.", process.pid, oldStatus.type, process.status.type)

            if (process.status != oldStatus) {
                processStatusHook(process, oldStatus, process.status)
            }
        }
    }

    /**
     * 进程主动状态变化钩子
     *
     *仅使用挂起按钮和阻塞时发送处理
     *
     * @param process PCB 进程
     * @param oldStatus ProcessStatus 旧的状态
     * @param newStatus ProcessStatus 新的状态
     */
    abstract fun processStatusHook(process: T, oldStatus: ProcessStatus, newStatus: ProcessStatus)

    /**
     * 根据pid 得到进程
     *
     * @param pid Long
     * @return T
     */
    private fun getPcbByPid(pid: Long): T {
        val filter = startedProcess.filter { it.pid == pid }
        return if (filter.isEmpty()) {
            throw IndexOutOfBoundsException("未找到合适的Pid")
        } else {
            filter[0]
        }
    }

    /**
     * cpu 占用率
     */
    override val cpuUsage: Double
        get() {
            return try {
                synchronized(allProcesses) {
                    var time = 0L
                    allProcesses.forEach {
                        time += it.usedCpuTime

                    }
                    return time.toDouble() / manager.runTime.toDouble()
                }
            } catch (e: Exception) {
                0.0
            }
        }


}
