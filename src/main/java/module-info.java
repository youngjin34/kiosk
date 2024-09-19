module org.zerock.kioskproject {
  requires javafx.controls;
  requires javafx.fxml;
  requires java.sql;

  opens org.zerock.kioskproject to javafx.fxml;
  exports org.zerock.kioskproject;
}
