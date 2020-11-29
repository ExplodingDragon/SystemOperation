package tech.openEdgn.test.activities

import com.github.open_edgn.fx.manager.activity.FXMLActivity
import com.jfoenix.controls.JFXComboBox
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.scene.layout.VBox


class StartActivity : FXMLActivity<VBox>() {
    @FXML
    private lateinit var process: JFXComboBox<String>

    @FXML
    private lateinit var memory: JFXComboBox<String>

    @FXML
    fun start(actionEvent: ActionEvent) {

    }

    override fun onCreate() {
        process.items.addAll("先来先服务","时间片轮转","高相应比优先","进程优先级","多级调度")
        memory.items.addAll("首次适应算法","循环适应算法","最佳适应算法","最坏适应算法")
        process.selectionModel.select(0)
        memory.selectionModel.select(0)
    }

    override val fxmlPath: String = "/fxml/activity_main.fxml"
    override val title: String = "选择调度算法"
    override val iconPath: String = "/icons/main.png"
}