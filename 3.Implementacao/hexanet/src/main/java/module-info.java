module org.hexanet.eventhub {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires jakarta.persistence;
    requires org.hibernate.orm.core;
    requires jbcrypt;

    opens org.hexanet.eventhub to javafx.fxml;
    opens org.hexanet.eventhub.controller to javafx.fxml;
    opens org.hexanet.eventhub.model to org.hibernate.orm.core, javafx.base;
    exports org.hexanet.eventhub;
}