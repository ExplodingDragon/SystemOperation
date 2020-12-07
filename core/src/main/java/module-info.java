module test.system {
    requires java.base;
    requires javafx.base;
    requires javafx.fxml;
    requires kotlin.stdlib;
    requires kotlin.reflect;
    requires logger4k.console;
    requires logger4k.core;
    requires fx.ui.manager;
    requires com.jfoenix;
    requires javafx.graphics;
    requires javafx.controls;
    exports tech.openEdgn.test.activities;
    opens tech.openEdgn.test.activities;
    opens tech.openEdgn.test.system;
    opens tech.openEdgn.test.system.pcbs ;
    opens tech.openEdgn.test.system.manager;
    opens tech.openEdgn.test.system.memory;
    opens tech.openEdgn.test.system.memory.impl;
    opens tech.openEdgn.test.system.process;
    opens tech.openEdgn.test.system.process.impl;
    opens fxml to fx.ui.manager;
    opens css to fx.ui.manager;
    opens icons to fx.ui.manager;
}