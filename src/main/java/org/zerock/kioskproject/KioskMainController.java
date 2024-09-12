package org.zerock.kioskproject;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class KioskMainController {
    private KioskMain mainApp;

    @FXML
    private Button switchToOrderButton;

    @FXML
    private Button switchToInfoButton; // FXML 버튼과 연결

    @FXML
    private void initialize() {
        switchToOrderButton.setOnAction(e -> {
            if (mainApp != null) {
                mainApp.switchToOrderScene();
            } else {
                System.err.println("MainApp is null");
            }
        });

        switchToInfoButton.setOnAction(e -> {
            if (mainApp != null) {
                mainApp.switchToInfoScene();
            } else {
                System.err.println("MainApp is null");
            }
        });
    }

    public void setMainApp(KioskMain mainApp) {
        this.mainApp = mainApp;
    }
}
