package tech.openEdgn.test.system.process

import tech.openEdgn.test.system.PCB
import tech.openEdgn.test.system.ProcessStatus
import tech.openEdgn.test.system.manager.ISystemManager
import tech.openEdgn.test.system.memory.IMemoryAlgorithm
import java.util.*

abstract class CustomAlgorithm<T : PCB>(memoryAlgorithm: IMemoryAlgorithm, manager: ISystemManager) :
        SimpleProcessAlgorithm<T>(memoryAlgorithm, manager) {
    protected val readyProcess: LinkedList<T> = LinkedList()

    /**
     *  清空就绪队列下的非就绪进程
     */
    protected fun clearUnReadyProcess() {
        readyProcess.removeIf {
            it.status != ProcessStatus.RUN_READY &&
                    it.status != ProcessStatus.RUN
        }
    }

    /**
     *  将符合条件的进程添加到就绪队列下
     */
    protected fun addReadyProcess() {
        noCreateProcess.forEach {
            if (it.startTime <= manager.runTime) {
                if (tryBootProcess(it)) {
                    readyProcess.addLast(it)
                }
            }
        }
    }

    /**
     * 移除运行完成的进程
     */
    protected fun removeFinishedProcess() {
        startedProcess
                .filter { it.needTime - 1 <= it.usedCpuTime }
                .forEach {
                    readyProcess.remove(it)
                    finishProcess(it)
                }
    }

    override fun processStatusHook(process: T, oldStatus: ProcessStatus, newStatus: ProcessStatus) {
        if (oldStatus == ProcessStatus.RUN_READY) {
            readyProcess.remove(process)
        }
        if (newStatus == ProcessStatus.RUN_READY) {
            readyProcess.addLast(process)
        } else {
            readyProcess.remove(process)
        }
    }

    override fun close() {
        super.close()
        readyProcess.clear()
    }
}