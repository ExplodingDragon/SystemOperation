package tech.openEdgn.test.system.process

import com.github.openEdgn.logger4k.getLogger
import tech.openEdgn.test.system.PCB
import tech.openEdgn.test.system.manager.ISystemManager
import tech.openEdgn.test.system.manager.ProcessAction
import tech.openEdgn.test.system.memory.IMemoryAlgorithm
import java.io.Closeable
import kotlin.reflect.KClass

/**
 * 进程调度算法接口
 */
abstract class BaseProcessAlgorithm<T : PCB>(
        protected val memoryAlgorithm: IMemoryAlgorithm,
        protected val manager: ISystemManager
) : Closeable {




    /**
     * 正在运行的进程
     */
    protected abstract val runProcess: List<T>

    /**
     * 全部的进程
     */
    abstract val allProcesses: MutableList<T>

    private val logger = getLogger()



    /**
     * 已启动的进程
     */
    protected abstract val startedProcess: List<T>

    /**
     * 未启动进程
     */
    protected abstract  val noCreateProcess: List<T>

    /**
     * 运行一个时钟周期
     */
    abstract fun runClockCycle()

    /**
     * 添加一个随机进程
     * @return PCB
     */
    abstract fun addRandomProcess()


    abstract  fun sendAction(pid: Long, action: ProcessAction)




    /**
     * cpu 占用率
     */
    abstract val cpuUsage: Double

    /**
     * 展示在 UI 上列表属性
     */
    abstract val displayClass: KClass<T>


}
