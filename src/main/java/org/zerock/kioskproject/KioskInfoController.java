package org.zerock.kioskproject;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class KioskInfoController {
    private KioskMain mainApp;

    @FXML
    private Label welcomeText;

    // 뒤로가기 버튼을 처리하는 메서드
    @FXML
    private void handleBackButton() {
        if (mainApp != null) {
            mainApp.switchToMainScene();  // 메인 화면으로 전환
        } else {
            System.err.println("MainApp is null");
        }
    }

    public void setMainApp(KioskMain mainApp) {
        this.mainApp = mainApp;
    }
}
