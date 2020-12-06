package tech.openEdgn.test.activities

import com.github.open_edgn.fx.manager.activity.FXMLActivity
import com.jfoenix.controls.JFXButton
import com.jfoenix.controls.JFXComboBox
import com.jfoenix.controls.JFXProgressBar
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.value.ObservableValue
import javafx.collections.FXCollections
import javafx.fxml.FXML
import javafx.scene.control.*
import javafx.scene.control.cell.PropertyValueFactory
import javafx.scene.layout.VBox
import javafx.util.Callback
import tech.openEdgn.test.system.Column
import tech.openEdgn.test.system.PCB
import tech.openEdgn.test.system.ProcessStatus
import tech.openEdgn.test.system.manager.ISystemManager
import tech.openEdgn.test.system.manager.ProcessAction
import tech.openEdgn.test.system.manager.SystemManager
import tech.openEdgn.test.system.memory.IMemoryAlgorithm
import tech.openEdgn.test.system.process.BaseProcessAlgorithm
import java.util.*
import kotlin.reflect.KClass


class MainActivity : FXMLActivity<VBox>(), Runnable {
    override val fxmlPath: String = "/fxml/activity_main.fxml"
    override val title: String = "进程模拟"
    override val stylePaths = arrayOf("/css/style_main.css")

    @FXML
    private lateinit var processAlgorithmLabel: Label

    @FXML
    private lateinit var memoryAlgorithmLabel: Label

    @FXML
    private lateinit var cpuUsageLabel: Label

    @FXML
    private lateinit var processSizeLabel: Label

    @FXML
    private lateinit var runTimeLabel: Label

    @FXML
    private lateinit var memorySizeProgressBar: JFXProgressBar

    @FXML
    private lateinit var cpuSizeProgressBar: JFXProgressBar

    @FXML
    private lateinit var memoryInfoLabel: Label

    @FXML
    private lateinit var memorySizeLabel: Label

    @FXML
    private lateinit var hangProcessLabel: Label

    @FXML
    private lateinit var processItemVBox: VBox

    @FXML
    private lateinit var waitProcessLabel: Label

    @FXML
    private lateinit var finishProcessLabel: Label

    @FXML
    private lateinit var spitProcessComboBox: JFXComboBox<ProcessStatus>

    @FXML
    private lateinit var table: TableView<PCB>

    @FXML
    private lateinit var selectProcessIdLabel: Label

    @FXML
    private lateinit var selectProcessNameLabel: Label

    @FXML
    private lateinit var selectProcessStopButton: JFXButton

    @FXML
    private lateinit var selectProcessWaitButton: JFXButton

    @Volatile
    private var chooseStatus = ProcessStatus.ALL


    private lateinit var timer: Timer

    private val processList = FXCollections.observableArrayList<PCB>()

    private val manager: ISystemManager by lazy {
        SystemManager(
                intent.getExtra(BaseProcessAlgorithm::class.simpleName!!, BaseProcessAlgorithm::class.java),
                intent.getExtra(IMemoryAlgorithm::class.simpleName!!, IMemoryAlgorithm::class.java),
        )
    }

    override fun onCreate() {
        table.items = processList
        memoryAlgorithmLabel.text =
                intent.getExtra(IMemoryAlgorithm::class.qualifiedName!!, "UNKNOWN Algorithm")
        processAlgorithmLabel.text =
                intent.getExtra(BaseProcessAlgorithm::class.qualifiedName!!, "UNKNOWN Algorithm")
        window.title = "进程模拟 - ${processAlgorithmLabel.text} - ${memoryAlgorithmLabel.text}"
        initTableUI(manager.displayClass)
        val callback = Callback<ListView<ProcessStatus>, ListCell<ProcessStatus>> {
            object : ListCell<ProcessStatus>() {
                override fun updateItem(item: ProcessStatus?, empty: Boolean) {
                    super.updateItem(item, empty)
                    if (item == null || empty) {
                        graphic = null
                    } else {
                        text = item.type
                    }
                }
            }
        }
        spitProcessComboBox.buttonCell = callback.call(null)
        spitProcessComboBox.cellFactory = callback
        spitProcessComboBox.items = FXCollections.observableArrayList(*ProcessStatus.values())
        spitProcessComboBox.selectionModel.select(0)
        chooseAlgorithm()
    }

    override fun onStart() {
        timer = Timer()
        timer.schedule(object : TimerTask() {
            override fun run() {
                runUiOnThread { this@MainActivity.run() }
            }
        }, 0L, 300L)
    }

    override fun onHide() {
        timer.cancel()
    }

    override fun run() {
        runTimeLabel.text = String.format("%02d:%02d", manager.runTime / 60, manager.runTime % 60)
        cpuUsageLabel.text = String.format("%.2f%%", manager.cpuUsage * 100)
        cpuSizeProgressBar.progress = manager.cpuUsage
        processSizeLabel.text = manager.processSize.toString()
        val memoryUsage = manager.memoryUsageSize
        val memorySize = manager.memorySize
        val used = memoryUsage.toDouble() / memorySize.toDouble()
        memorySizeProgressBar.progress = used
        memoryInfoLabel.text = "$memoryUsage / $memorySize"
        memorySizeLabel.text = String.format("%.2f%%", used * 100)
        hangProcessLabel.text = manager.hangProcessSize.toString()
        waitProcessLabel.text = manager.waitProcessSize.toString()
        finishProcessLabel.text = manager.finishProcessSize.toString()
        chooseAlgorithm()
        chooseDetails()
    }

    @Volatile
    private lateinit var selectItem: PCB

    private fun chooseDetails() {
        processItemVBox.isDisable = true
        table.selectionModel?.selectedItem?.run {
            if (pid == 0L) {
                return
            }
            selectItem = this
            processItemVBox.isDisable = false
            selectProcessIdLabel.text = pid.toString()
            selectProcessNameLabel.text = name
            when (status) {
                ProcessStatus.RUN,
                ProcessStatus.CREATE,
                ProcessStatus.RUN_READY,
                ProcessStatus.RUN_WAIT -> {
                    selectProcessStopButton.isDisable = false
                    selectProcessStopButton.text = "挂起"
                }
                ProcessStatus.STOP_WAIT,
                ProcessStatus.STOP_READY,
                -> {
                    selectProcessStopButton.isDisable = false
                    selectProcessStopButton.text = "取消挂起"
                }
                else -> {
                    selectProcessStopButton.isDisable = true
                    selectProcessStopButton.text = "此状态无法挂起"
                }
            }
            when (status) {
                ProcessStatus.RUN -> {
                    selectProcessWaitButton.isDisable = false
                    selectProcessWaitButton.text = "阻塞"
                }
                ProcessStatus.RUN_WAIT,
                ProcessStatus.STOP_WAIT -> {
                    selectProcessWaitButton.isDisable = false
                    selectProcessWaitButton.text = "取消阻塞"
                }
                else -> {
                    selectProcessWaitButton.isDisable = true
                    selectProcessWaitButton.text = "此状态无法阻塞"
                }
            }

        }
    }


    @FXML
    fun addRandomAction() {
        manager.addRandomProcess()
        logger.info("切换进程列表为: {},当前列个数为 {}.",
                spitProcessComboBox.selectionModel.selectedItem.type, table.items.size)

    }

    @FXML
    fun selectProcessStopAction() {
        manager.sendAction(selectItem.pid,ProcessAction.STOP)
    }

    @FXML
    fun selectProcessWaitAction() {
        manager.sendAction(selectItem.pid,ProcessAction.WAIT)
    }

    @FXML
    fun spitProcessAction() {
        chooseStatus = spitProcessComboBox.selectionModel.selectedItem
        chooseAlgorithm()
    }

    /**
     * 切换进程分割
     */
    private fun chooseAlgorithm() {
        manager.allProcess.forEach {
            if (!processList.contains(it)) {
                processList.add(it)
            }
        }
        processList.removeIf {
            if (chooseStatus == ProcessStatus.ALL) {
                false
            } else {
                it.status != chooseStatus
            }
        }
        processList.sorted { o1, o2 ->
            (o1.pid - o2.pid).toInt()
        }
        table.refresh()
    }

    /**
     * 初始化展示的 table
     *
     * @param displayClass KClass<out Any>
     */
    private fun initTableUI(displayClass: KClass<out Any>) {
        val columns = table.columns
        columns.clear()
        val declaredFields = displayClass.javaObjectType.declaredFields
        for (declaredField in declaredFields) {
            declaredField.getAnnotation(Column::class.java)?.let { column ->
                // 此字段可用
                val tableColumn = TableColumn<PCB, Any>(column.name)
                tableColumn.cellValueFactory = Callback<TableColumn.CellDataFeatures<PCB, Any>,
                        ObservableValue<Any>> {
                    val call = PropertyValueFactory<PCB, Any>(declaredField.name).call(it)
                    val value = call.value
                    SimpleObjectProperty(column.format.formatImpl.formatStr(value))
                }
                tableColumn.isSortable = false
                columns.add(tableColumn)
            }
        }
    }

}