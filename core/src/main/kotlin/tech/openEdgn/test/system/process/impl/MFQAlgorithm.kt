package tech.openEdgn.test.system.process.impl

import tech.openEdgn.test.system.ProcessStatus
import tech.openEdgn.test.system.manager.ISystemManager
import tech.openEdgn.test.system.memory.IMemoryAlgorithm
import tech.openEdgn.test.system.pcbs.MFQPCB
import tech.openEdgn.test.system.process.SimpleProcessAlgorithm
import java.util.*
import kotlin.math.abs

class MFQAlgorithm(memoryAlgorithm: IMemoryAlgorithm, manager: ISystemManager) :
        SimpleProcessAlgorithm<MFQPCB>(memoryAlgorithm, manager) {

    //    private val readyOneProcess = LinkedList<>
    private val readyProcesses = listOf<Processes>(
            Processes("第一队列", 3),
            Processes("第二队列", 6),
            Processes("第三队列", 10)
    )

    data class Processes(
            val name: String,
            val spitTime: Long,
            val readyProcesses: LinkedList<MFQPCB> = LinkedList<MFQPCB>()
    )

    override fun runClockCycle() {
        clearUnReadyProcess()
        // 处理未在规定时间完成的进程
        for ((i, v) in readyProcesses.withIndex()) {
            val filter = v.readyProcesses.filter { it.usedSpitTime >= v.spitTime }
            if (v != readyProcesses.last()) {
                v.readyProcesses.removeAll(filter)
                filter.forEach {
                    it.usedSpitTime = 0
                    it.status = ProcessStatus.RUN_READY
                    it.lineId = i.toLong() + 2
                    readyProcesses[i + 1].readyProcesses.addLast(it)
                }
            } else {
                v.readyProcesses.removeAll(filter)
                filter.forEach {
                    it.usedSpitTime = 0
                    it.status = ProcessStatus.RUN_READY
                    it.lineId = i.toLong() + 1
                    v.readyProcesses.addLast(it)
                }
            }
        }
        addReadyProcess()
        if (runProcess.isNotEmpty()) {
            val first = runProcess.first()
            first.run {
                usedCpuTime++
                usedSpitTime++
            }
        }
        removeFinishedProcess()
    }

    override val runProcess: List<MFQPCB>
        get() {
            val f = getF()
            if (f.second.isNotEmpty()) {
                val first = f.second.first()
                if (first.status == ProcessStatus.RUN) {
                    return f.second
                } else {
                    if (super.runProcess.isNotEmpty()) {
                        super.runProcess.first().status = ProcessStatus.RUN_READY
                        f.first.readyProcesses.run {
                            removeAll(f.second)
                            addLast(f.second.first())
                        }
                    }
                    first.status = ProcessStatus.RUN
                }
            }
            return emptyList()
        }

    private fun getF(): Pair<Processes, List<MFQPCB>> {
        for (readyProcess in readyProcesses) {
            for (pcb in readyProcess.readyProcesses) {
                return Pair(readyProcess, listOf(pcb))
            }
        }
        return Pair(readyProcesses.first(), emptyList())
    }

    private fun removeFinishedProcess() {
        startedProcess
                .filter { isFinished(it) }
                .forEach {
                    for (readyProcess in readyProcesses) {
                        it.lineId = 0
                        readyProcess.readyProcesses.remove(it)
                    }
                    finishProcess(it)
                }
    }

    private fun addReadyProcess() {
        noCreateProcess.forEach {
            if (it.startTime <= manager.runTime) {
                if (tryBootProcess(it)) {
                    it.lineId = 1
                    readyProcesses.first().readyProcesses.addLast(it)
                }
            }
        }
    }

    private fun clearUnReadyProcess() {
        readyProcesses.forEach {
            it.readyProcesses.removeIf { pcb ->
                pcb.status != ProcessStatus.RUN_READY &&
                        pcb.status != ProcessStatus.RUN
            }
        }
    }


    override fun addRandomProcess() {
        allProcesses.add(MFQPCB(name = "进程 ${System.currentTimeMillis() % 1000}",
                startTime = manager.runTime + abs(Random().nextInt() % 10 + 12),
                needTime = abs(abs(Random().nextLong()) % 10 + 2),
                needMemory = abs(abs(Random().nextInt() % 80) + 30)
        ))
    }

    override fun processStatusHook(process: MFQPCB, oldStatus: ProcessStatus, newStatus: ProcessStatus) {
        if (oldStatus == ProcessStatus.RUN_READY) {
            readyProcesses.forEach { it.readyProcesses.remove(process) }
        }
        if (newStatus == ProcessStatus.RUN_READY) {
            readyProcesses.first().readyProcesses.addLast(process)
        } else {
            readyProcesses.forEach { it.readyProcesses.remove(process) }
        }
    }

    override val displayClass = MFQPCB::class
}