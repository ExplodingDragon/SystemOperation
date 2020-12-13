package tech.openEdgn.test.component

import javafx.scene.canvas.Canvas
import javafx.scene.canvas.GraphicsContext
import javafx.scene.layout.VBox

abstract class FXCanvas : VBox() {
    private val canvas = Canvas()
    private val graphics: GraphicsContext = canvas.graphicsContext2D

    init {
        val mWidth = 200.0
        val mHeight = 100.0
        prefWidth = mWidth
        maxWidth = Double.MAX_VALUE
        minWidth = mWidth
        prefHeight = mHeight
        maxHeight = Double.MAX_VALUE
        minWidth = mWidth
        children.add(canvas)
        widthProperty().addListener { _, _, newValue ->
            canvas.widthProperty().set(newValue.toDouble()- (padding.left + padding.right))
            draw()
        }
        heightProperty().addListener { _, _, newValue ->
            canvas.heightProperty().set(newValue.toDouble() - (padding.top + padding.bottom))
            draw()
        }
    }

    abstract fun draw(graphics: GraphicsContext)

    protected fun draw() {
        graphics.clearRect(0.0, 0.0, canvas.width, canvas.height)
        draw(graphics)
    }

    protected val drawWidth: Double
        get() {
            return canvas.width
        }

    protected val drawHeight: Double
        get() {
            return canvas.height
        }

}