package tech.openEdgn.test.system.manager

import tech.openEdgn.test.system.PCB
import tech.openEdgn.test.system.ProcessStatus
import tech.openEdgn.test.system.memory.IMemoryAlgorithm
import tech.openEdgn.test.system.process.BaseProcessAlgorithm
import java.io.Closeable
import java.lang.RuntimeException
import java.util.*
import java.util.concurrent.Executors
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.primaryConstructor

/**
 *
 * 进程启动器
 *
 */
class SystemManager(processImpl: Class<out BaseProcessAlgorithm<out PCB>>, memoryImpl: Class<out IMemoryAlgorithm>) : ISystemManager, Runnable, Closeable {
    private val threadPool = Executors.newCachedThreadPool()
    private val memoryAlgorithm: IMemoryAlgorithm = memoryImpl.kotlin.createInstance()
    private val processAlgorithm: BaseProcessAlgorithm<out PCB> by lazy {
        processImpl.kotlin.primaryConstructor
                ?.call(memoryAlgorithm, this) ?: throw RuntimeException("Error")
    }

    override val waitProcessSize: Int
        get() {
            return allProcess.filter {
                it.status == ProcessStatus.RUN_WAIT
            }.size
        }
    override val hangProcessSize: Int
        get() {
            return allProcess.filter {
                it.status == ProcessStatus.STOP_WAIT || it.status == ProcessStatus.STOP_READY
            }.size
        }
    override val memorySize: Int
        get() = memoryAlgorithm.memorySize
    override val memoryUsageSize: Int
        get() = memoryAlgorithm.memoryUsageSize

    override val finishProcessSize: Int
        get() {
            return allProcess.filter {
                it.status == ProcessStatus.FINISH
            }.size
        }

    override val processSize: Int
        get() = allProcess.size
    override val cpuUsage: Double
        get() = processAlgorithm.cpuUsage

    @Volatile
    private var clockCycleTime = 0L

    override val runTime: Long
        get() = clockCycleTime

    override val allProcess: List<PCB>
        get() = processAlgorithm.allProcesses

    override val displayClass: KClass<*>
        get() = processAlgorithm.displayClass
    private val timer = Timer()

    override fun run() {
        processAlgorithm.runClockCycle()

        clockCycleTime += 1
    }

    override fun addRandomProcess() {
        threadPool.submit {
            processAlgorithm.addRandomProcess()
        }
    }

    init {
        timer.schedule(object : TimerTask() {
            override fun run() {
                this@SystemManager.run()
            }
        }, 0L, 1000L)
    }

    override fun sendAction(pid: Long, action: ProcessAction) {
        threadPool.submit {
            processAlgorithm.sendAction(pid, action)
        }
    }

    override fun close() {
        threadPool.shutdownNow()
        timer.cancel()
        processAlgorithm.close()
    }
}