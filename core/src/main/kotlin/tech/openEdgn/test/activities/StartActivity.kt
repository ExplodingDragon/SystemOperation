package tech.openEdgn.test.activities

import com.github.open_edgn.fx.manager.activity.FXMLActivity
import com.github.open_edgn.fx.manager.intent.Intent
import com.jfoenix.controls.JFXComboBox
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.scene.layout.VBox
import tech.openEdgn.test.system.memory.IMemoryAlgorithm
import tech.openEdgn.test.system.memory.MemoryAlgorithmInfo
import tech.openEdgn.test.system.process.IProcessAlgorithm
import tech.openEdgn.test.system.process.ProcessAlgorithmInfo


class StartActivity : FXMLActivity<VBox>() {


    @FXML
    private lateinit var process: JFXComboBox<ProcessAlgorithmInfo>


    @FXML
    private lateinit var memory: JFXComboBox<MemoryAlgorithmInfo>

    @FXML
    fun start(actionEvent: ActionEvent) {
        startActivity(Intent(this, MainActivity::class)
                .putExtra(IMemoryAlgorithm::class.simpleName!!, memory.selectionModel.selectedItem)
                .putExtra(IProcessAlgorithm::class.simpleName!!, process.selectionModel.selectedItem)
        )
    }

    override fun onCreate() {
        process.items.addAll(
                ProcessAlgorithmInfo("先来先服务调度算法", IProcessAlgorithm::class),
                ProcessAlgorithmInfo("时间片轮转调度算法", IProcessAlgorithm::class),
                ProcessAlgorithmInfo("高响应比优先调度算法", IProcessAlgorithm::class),
                ProcessAlgorithmInfo("优先级调度算法", IProcessAlgorithm::class),
                ProcessAlgorithmInfo("多级反馈队列调度算法", IProcessAlgorithm::class)
        )
        memory.items.addAll(
                MemoryAlgorithmInfo("首次适应算法", IMemoryAlgorithm::class),
                MemoryAlgorithmInfo("循环适应算法", IMemoryAlgorithm::class),
                MemoryAlgorithmInfo("最佳适应算法", IMemoryAlgorithm::class),
                MemoryAlgorithmInfo("最坏适应算法", IMemoryAlgorithm::class)


        )

        process.selectionModel.select(0)
        memory.selectionModel.select(0)
    }

    override val fxmlPath: String = "/fxml/activity_start.fxml"
    override val title: String = "选择调度算法"
    override val iconPath: String = "/icons/main.png"
}

