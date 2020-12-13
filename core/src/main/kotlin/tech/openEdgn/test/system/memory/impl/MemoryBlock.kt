package tech.openEdgn.test.system.memory.impl

data class MemoryBlock(
    @Volatile
    var pid: Long,
    @Volatile
    var offset: Int,
    @Volatile
    var len: Int
) {
    val isFree: Boolean
        get() = pid == 0L
}
