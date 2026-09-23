module com.movieticketbooking {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens com.movieticketbooking to javafx.fxml;
    exports com.movieticketbooking;
}
