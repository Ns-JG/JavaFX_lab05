module com.nsjg.javafx_lab05 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires com.almasb.fxgl.all;
    requires java.desktop;

    opens com.nsjg.javafx_lab05 to javafx.fxml;
    exports com.nsjg.javafx_lab05;
}