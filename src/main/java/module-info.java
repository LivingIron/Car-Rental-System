module org.group29 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens org.group29 to javafx.fxml;
    exports org.group29;
    exports org.group29.entities;
}
