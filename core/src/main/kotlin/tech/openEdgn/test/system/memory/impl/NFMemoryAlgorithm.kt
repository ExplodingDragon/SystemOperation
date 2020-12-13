package tech.openEdgn.test.system.memory.impl

import tech.openEdgn.test.system.PCB
import tech.openEdgn.test.system.memory.IMemoryAlgorithm
import java.util.*

class NFMemoryAlgorithm : IMemoryAlgorithm() {

    private val list = LinkedList<MemoryBlock>()

    init {
        list.add(MemoryBlock(0, 0, memorySize))
    }

    @Synchronized
    override fun finishProcess(process: PCB) {
        val item = list.first { it.pid == process.pid }
        val index = list.indexOf(item)
        var executeIt = false
        if (index > 0) {
            val block = list[index - 1]
            if (block.isFree) {
                block.len += item.len
                executeIt = true
            }
        }
        if (!executeIt && index < list.size - 1) {
            val block = list[index + 1]
            if (block.isFree) {
                block.len += item.len
                executeIt = true
            }
        }
        if (!executeIt) {
            item.pid = 0
        }
        process.memoryOffset = 0
    }


    override val memoryUsageSize: Int
        get() {
            var ret = 0
            list.filter { it.pid != 0L }.forEach { ret += it.len }
            return ret
        }


    private var lastSearchId = 0

    @Synchronized
    override fun tryBootProcess(process: PCB): Boolean {
        return if (searchWithIndex(lastSearchId, process)) {
            true
        } else {
            return searchWithIndex(0, process)
        }

    }

    private fun searchWithIndex(i: Int, process: PCB): Boolean {

        for ((index, memoryBlock) in list.withIndex()) {
            if (index < i) {
                continue
            }
            if (memoryBlock.len > process.needMemory && memoryBlock.isFree) {
                list.add(
                    index + 1,
                    MemoryBlock(
                        0, memoryBlock.offset + process.needMemory,
                        (memoryBlock.len - process.needMemory)
                    )
                )
                memoryBlock.len = process.needMemory
                memoryBlock.pid = process.pid
                process.memoryOffset = memoryBlock.offset
                lastSearchId = index
                return true
            } else if (memoryBlock.len == process.needMemory && memoryBlock.isFree) {
                memoryBlock.pid = process.pid
                process.memoryOffset = memoryBlock.offset
                lastSearchId = index
                return true
            }
        }
        return false
    }


}