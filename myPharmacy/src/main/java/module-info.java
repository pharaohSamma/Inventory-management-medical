module com.mypharmacy {
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
    requires java.naming;
    requires org.hibernate.orm.core;

    opens com.mypharmacy to javafx.fxml;
    exports com.mypharmacy;
    exports com.mypharmacy.models;
    opens com.mypharmacy.models to javafx.fxml, org.hibernate.orm.core;
    exports com.mypharmacy.controllers;
    opens com.mypharmacy.controllers to javafx.fxml;

}