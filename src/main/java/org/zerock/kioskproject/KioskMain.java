package org.zerock.kioskproject;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class KioskMain extends Application {
    private Stage primaryStage;
    private Scene mainScene;
    private Scene orderScene;
    private Scene infoScene;

    @Override
    public void start(Stage stage) throws IOException {
        this.primaryStage = stage;

        // 메인 화면 로드
        FXMLLoader mainLoader = new FXMLLoader(getClass().getResource("KioskMain.fxml"));
        mainScene = new Scene(mainLoader.load(), 700, 950);

        // 주문 화면 로드
        FXMLLoader orderLoader = new FXMLLoader(getClass().getResource("KioskOrder.fxml"));
        orderScene = new Scene(orderLoader.load(), 700, 950);

        // 정보 화면 로드
        FXMLLoader infoLoader = new FXMLLoader(getClass().getResource("KioskInfo.fxml"));
        infoScene = new Scene(infoLoader.load(), 700, 950);

        // 메인 화면 컨트롤러 설정
        KioskMainController mainController = mainLoader.getController();
        mainController.setMainApp(this);

        // 주문 화면 컨트롤러 설정
        KioskOrderController orderController = orderLoader.getController();
        orderController.setMainApp(this);

        // 정보 화면 컨트롤러 설정
        KioskInfoController infoController = infoLoader.getController();
        infoController.setMainApp(this);

        // 초기 화면 설정
        stage.setTitle("Kiosk Application");
        stage.setScene(mainScene);
        stage.show();
    }

    public void switchToOrderScene() {
        primaryStage.setScene(orderScene);
    }

    public void switchToInfoScene() {
        primaryStage.setScene(infoScene);
    }

    public void switchToMainScene() {
        primaryStage.setScene(mainScene);  // 메인 화면으로 전환
    }

    public static void main(String[] args) {
        launch();
    }
}
