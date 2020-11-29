package tech.openEdgn.test.activities

import com.github.open_edgn.fx.manager.activity.FXMLActivity
import javafx.scene.layout.VBox

class MainActivity: FXMLActivity<VBox>() {
    override val fxmlPath: String = "/fxml/activity_main.fxml"
    override val title: String = "主页"
    override val stylePaths = arrayOf("/css/style_main.css")
}