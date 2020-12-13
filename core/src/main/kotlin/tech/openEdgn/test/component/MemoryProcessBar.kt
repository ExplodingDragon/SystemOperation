package tech.openEdgn.test.component

import javafx.application.Platform
import javafx.event.EventHandler
import javafx.scene.canvas.GraphicsContext
import javafx.scene.input.MouseEvent
import javafx.scene.paint.Color
import tech.openEdgn.test.system.PCB
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.Executors

class MemoryProcessBar(private val memorySize: Long) : FXCanvas(), EventHandler<MouseEvent> {
    private val threadPool = Executors.newCachedThreadPool()
    private val block = ConcurrentHashMap<Long, MemoryBlock>()

    init {
        onMouseMoved = this
    }

    var memoryMovedListener: (PCB) -> Unit = {}


    data class MemoryBlock(
        val pid: Long,
        val name: String,
        val startIndex: Int,
        var length: Int,
        val pcb: PCB
    )

    fun update(pcb: List<PCB>) {
        val list = pcb.filter { it.pid > 0 }.toMutableList()
        threadPool.run {

            synchronized(block) {
                block.clear()
                for (block1 in list) {
                    block[block1.pid] =
                        MemoryBlock(block1.pid, block1.name, block1.memoryOffset, block1.needMemory, block1)
                }
            }
            Platform.runLater {
                draw()
            }
        }


    }

    override fun draw(graphics: GraphicsContext) {
        graphics.fill = Color.ANTIQUEWHITE
        graphics.fillRect(0.0, 0.0, drawWidth, drawHeight)
        synchronized(block) {
            for (value in block.values) {
                draw(
                    graphics, value.pcb,
                    value.startIndex, value.length, value.pid != 0L
                )
            }
        }
    }

    private fun draw(graphics: GraphicsContext, pcb: PCB, offset: Int, len: Int, used: Boolean) {

        val xSpit = drawWidth / memorySize
        if (used) {
            graphics.fill = Color.PINK
            val d = xSpit * offset
            val e = xSpit * len
            if (x > d && x < (d + e)) {
                graphics.fill = Color.RED
                hook(pcb)
            }
            graphics.fillRect(d, 0.0, e, drawHeight)
            graphics.lineWidth = 3.0

            graphics.fill = Color.BLACK
            graphics.strokeRect(d, 0.0, e, drawHeight)
        }
    }

    @Volatile
    private var pcbPid = 0L
    private fun hook(pcb: PCB) {
        if (pcbPid != pcb.pid) {
            memoryMovedListener(pcb)
            pcbPid = pcb.pid
        }
    }

    @Volatile
    var x = 0.0
    override fun handle(event: MouseEvent) {
        x = event.x
    }

}