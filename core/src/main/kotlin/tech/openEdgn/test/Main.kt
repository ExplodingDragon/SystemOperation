package tech.openEdgn.test

import com.github.openEdgn.logger4k.LoggerFactory
import com.github.open_edgn.fx.manager.FXBoot
import com.github.open_edgn.fx.manager.UIApplication
import tech.openEdgn.test.activities.StartActivity

class Main

fun main() {
    UIApplication.boot(FXBoot.Builder(StartActivity::class)
            .addStyleUrl(Main::class.java.getResource("/css/global.css"))
            .setIcon(Main::class.java.getResource("/icons/main.png")).build())
}