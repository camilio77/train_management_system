module co.edu.upb.train_management_system {
    requires javafx.controls;
    requires javafx.fxml;

    requires java.sql;
    requires java.rmi;

    opens co.edu.upb.train_management_system to javafx.fxml;
    exports co.edu.upb.train_management_system;
}
