module com.cbodo.schedule {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.java;

    opens com.cbodo.schedule to javafx.fxml;
    exports com.cbodo.schedule;
    exports com.cbodo.schedule.controller;
    opens com.cbodo.schedule.controller to javafx.fxml;
    exports com.cbodo.schedule.model;
    opens com.cbodo.schedule.model to javafx.base;
    exports com.cbodo.schedule.util;
    opens com.cbodo.schedule.util to javafx.fxml;
}