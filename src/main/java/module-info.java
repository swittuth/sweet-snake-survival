module com.programming.swittuth.snake_final_project {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.programming.swittuth.snake_final_project to javafx.fxml;
    exports com.programming.swittuth.snake_final_project;
}