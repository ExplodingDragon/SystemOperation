package tech.openEdgn.test.activities

import com.github.open_edgn.fx.manager.activity.FXMLActivity
import javafx.scene.layout.VBox

class StartActivity : FXMLActivity<VBox>() {
    override val fxmlPath: String = "/fxml/activity_main.fxml"
    override val title: String= "程序启动"
}