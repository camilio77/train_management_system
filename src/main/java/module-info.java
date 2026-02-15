module co.edu.upb.train_management_system {
    requires javafx.controls;
    requires javafx.fxml;

    opens co.edu.upb.train_management_system to javafx.fxml;
    exports co.edu.upb.train_management_system;
}
